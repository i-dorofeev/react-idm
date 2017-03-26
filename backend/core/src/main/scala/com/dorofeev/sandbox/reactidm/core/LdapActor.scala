package com.dorofeev.sandbox.reactidm.core

import akka.actor.{Actor, ActorRef, Cancellable, PoisonPill, Props}
import com.dorofeev.sandbox.reactidm.core.ConnectorObjectManagerActor.Removed
import com.dorofeev.sandbox.reactidm.core.Main.MyConnectorObject
import org.identityconnectors.framework.common.objects.{ConnectorObject, SyncToken, Uid}

import scala.collection.mutable
import scala.concurrent.duration._
import scala.language.postfixOps

object LdapActorInternalCommands {

}

class LdapActor() extends Actor {

  override def preStart(): Unit = {
    LdapConnector.initConnector()

    val connectorObjectManagerActor = context.actorOf(Props(new ConnectorObjectManagerActor()))
    context.actorOf(Props(new ReconciliationActor(connectorObjectManagerActor)))

    super.preStart()
  }

  override def receive: Receive = {
    case _ =>
  }
}

class ReconciliationActor(val connectorObjectManager: ActorRef) extends Actor {

  object ReconcileCmd

  import context.dispatcher

  var tick: Cancellable = _
  var reconciliationList: List[Uid] = List()

  override def preStart(): Unit = {
    tick = context.system.scheduler.schedule(1 seconds, 5 seconds, self, ReconcileCmd)
    super.preStart()
  }

  override def receive: Receive = {
    case ReconcileCmd =>

      var list = List[Uid]()
      
      println("starting reconciliation...")

      LdapConnector.search("inetOrgPerson",
        connectorObject => {
          list = connectorObject.getUid :: list
          connectorObjectManager ! connectorObject
        },
        () => {
          val removed = reconciliationList.filterNot(list.toSet)
          for (uid <- removed)
            connectorObjectManager ! ConnectorObjectManagerActor.Removed(uid)
          reconciliationList = list

          println("finished reconciliation")
        })
  }
}

object ConnectorObjectManagerActor {
  case class Removed(uid: Uid)
}

class ConnectorObjectManagerActor extends Actor {

  val objectActors: mutable.Map[Uid, ActorRef] = mutable.Map()

  override def receive: Receive = {
    case obj: ConnectorObject =>
      val objActorRef = objectActors.getOrElseUpdate(obj.getUid, createConnectorObjectActor(obj.getUid))
      objActorRef ! obj

    case Removed(uid) =>
      val objActorRef = objectActors.getOrElseUpdate(uid, createConnectorObjectActor(uid))
      objActorRef ! Removed
      objActorRef ! PoisonPill
      objectActors - uid
  }

  private def createConnectorObjectActor(uid: Uid) = context.actorOf(Props(new ConnectorObjectActor(uid)))
}

object ConnectorObjectActor {
  object Removed
}

class ConnectorObjectActor(val uid: Uid) extends Actor {

  var connectorObject: ConnectorObject = _

  override def receive: Receive = {
    case obj: ConnectorObject =>
      if (connectorObject == null) {
        println("New object created: " + obj)
      } else if (!connectorObject.getAttributes.equals(obj.getAttributes)) {
        println("Object " + obj.getName.getNameValue + " modified")
      }

      connectorObject = obj

    case Removed =>
      println("Object " + connectorObject.getName.getNameValue + " deleted")
      connectorObject = null
  }
}

class LdapLiveSyncActor extends Actor {

  import context.dispatcher

  var tick: Cancellable = _
  var syncToken: SyncToken = _

  override def preStart(): Unit = {
    tick = context.system.scheduler.schedule(1 seconds, 5 seconds, self, "sync")
    super.preStart()
  }

  override def postStop(): Unit = tick.cancel()
  
  override def receive: Receive = {

    case "sync" =>
      if (syncToken == null)
        syncToken = LdapConnector.getLatestSyncToken("inetOrgPerson")

      syncToken = LdapConnector.sync("inetOrgPerson", syncToken, new SynchronizationEventHandler {
        override def onCreateOrUpdate(resourceObject: MyConnectorObject): Unit = context.parent ! ResourceObjectAddedCreatedOrUpdated(resourceObject)
        override def onDelete(resourceObject: MyConnectorObject): Unit = context.parent ! ResourceObjectDeleted(resourceObject)
      })
  }
}



