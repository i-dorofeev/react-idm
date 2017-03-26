package com.dorofeev.sandbox.reactidm.core

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorRef, ActorSelection, Cancellable, Props}
import com.dorofeev.sandbox.reactidm.core.Main.ResourceObject
import org.identityconnectors.framework.common.objects.SyncToken

import scala.concurrent.duration._
import scala.language.postfixOps

class LdapActor(val reconciliationActorSelection: ActorSelection) extends Actor {

  import context.dispatcher

  var reconciliationActor: ActorRef = _
  var liveSyncActor: ActorRef = _

  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    LdapConnector.initConnector()
    reconciliationActorSelection.resolveOne(FiniteDuration(10, TimeUnit.SECONDS))
      .onSuccess({ case ref =>
        liveSyncActor = context.actorOf(Props(new LdapLiveSyncActor()))
        reconciliationActor = ref
      })
    super.preStart()
  }

  override def receive: Receive = {

    case msg =>
      if (sender() == liveSyncActor)
        receiveLiveSyncMsg(msg)
      else
        receiveExternalMsg(msg)
  }

  private def receiveLiveSyncMsg: Receive = {
    case msg => reconciliationActor ! msg
  }

  private def receiveExternalMsg: Receive = {
    case ReconcileCmd =>
      LdapConnector.search("inetOrgPerson",
        resourceObj => sender() ! ReconciliationObject(resourceObj),
        () => sender() ! ReconciliationFinished)
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
        override def onCreateOrUpdate(resourceObject: ResourceObject): Unit = context.parent ! ResourceObjectAddedCreatedOrUpdated(resourceObject)
        override def onDelete(resourceObject: ResourceObject): Unit = context.parent ! ResourceObjectDeleted(resourceObject)
      })
  }
}



