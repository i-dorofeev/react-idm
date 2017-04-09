package com.dorofeev.sandbox.reactidm.core

import akka.Done
import akka.actor.{Actor, ActorRef, Cancellable, PoisonPill, Props}
import akka.stream.ActorMaterializer
import com.dorofeev.sandbox.reactidm.core.ConnectorObjectManagerActor.Removed
import com.dorofeev.sandbox.reactidm.core.Main.MyConnectorObject
import org.identityconnectors.framework.common.objects.SyncToken

import scala.collection.mutable
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Success}

object LdapActorInternalCommands {

}

class LdapActor() extends Actor with Async {

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
  var reconciliationList: List[SAttributeUid] = List()

  override def preStart(): Unit = {
    tick = context.system.scheduler.schedule(1 seconds, 5 seconds, self, ReconcileCmd)
    super.preStart()
  }

  override def receive: Receive = {
    case ReconcileCmd =>

      var list = List[SAttributeUid]()

      println("starting reconciliation...")

      implicit val materializer = ActorMaterializer()

      LdapConnector.stream("inetOrgPerson")
        .runForeach({ co =>
          list = co.uid :: list
          connectorObjectManager ! co
        })
        .onSuccess({ case Done =>
            val removed = reconciliationList.filterNot(list.toSet)
            for (uid <- removed)
              connectorObjectManager ! ConnectorObjectManagerActor.Removed(uid)
            reconciliationList = list

            println("finished reconciliation")
        })

      /*
      LdapConnector.search("inetOrgPerson",
        connectorObject => {
          list = connectorObject.uid :: list
          connectorObjectManager ! connectorObject
        },
        () => {
          val removed = reconciliationList.filterNot(list.toSet)
          for (uid <- removed)
            connectorObjectManager ! ConnectorObjectManagerActor.Removed(uid)
          reconciliationList = list

          println("finished reconciliation")
        })
        */
  }
}

object ConnectorObjectManagerActor {
  case class Removed(uid: SAttributeUid)
}

class ConnectorObjectManagerActor extends Actor with Async {

  import context.dispatcher

  val objectActors: mutable.Map[SAttributeUid, ActorRef] = mutable.Map()
  val persistence: H2LdapActorPersistence = new H2LdapActorPersistence

  override def preStart(): Unit = {

    async { persistence.setupDb } onComplete {
      case Success(_) => // do nothing
      case Failure(ex) => println("==> Failed to initialize db: " + ex)
    }

    super.preStart()
  }

  override def receive: Receive = {
    case obj: SConnectorObject =>
      val objActorRef = objectActors.getOrElseUpdate(obj.uid, createConnectorObjectActor(obj.uid))
      objActorRef ! obj

    case Removed(uid) =>
      val objActorRef = objectActors.getOrElseUpdate(uid, createConnectorObjectActor(uid))
      objActorRef ! Removed
      objActorRef ! PoisonPill
      objectActors - uid
  }

  private def createConnectorObjectActor(uid: SAttributeUid) = context.actorOf(Props(new ConnectorObjectActor(uid, persistence)))
}

object ConnectorObjectActor {
  object Removed
}

class ConnectorObjectActor(val uid: SAttributeUid, val persistence: LdapActorPersistence) extends Actor with Async {

  import context.dispatcher

  var connectorObject: SConnectorObject = _

  override def preStart(): Unit = {

    async { persistence.restore(uid) } onComplete {
      case Success(Some(obj)) => connectorObject = obj
      case Success(_) => // do nothing
      case Failure(ex) => println("==> Failed to query db for uid=" + uid + ": " + ex)
    }

    super.preStart()
  }

  override def receive: Receive = {
    case obj: SConnectorObject =>
      if (connectorObject == null) {
        println("Received object created: " + obj)

        async { persistence.persist(obj) } onComplete {
            case Success(_) => println("Inserted connector object with uid=" + uid.value)
            case Failure(ex) => println("==> Failed to insert new connector obj for uid=" + uid + ": " + ex)
        }
      } else if (!connectorObject.attributes.equals(obj.attributes)) {
        println("Object " + obj.name.value + " modified")

        async { persistence.update(obj) } onComplete {
            case Success(_) => println("Updated connector object for uid=" + uid.value)
            case Failure(ex) => println("===> Failed to update connector obj for uid=" + uid.value + ": " + ex)
        }
      }

      connectorObject = obj

    case Removed =>
      println("Object " + connectorObject.name.value + " deleted")

      async { persistence.delete(uid) } onComplete {
          case Success(_) => println("Deleted connector object for uid=" + uid.value)
          case Failure(ex) => println("==> Failed to delete connector obj for uid=" + uid.value + ": " + ex)
      }

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





