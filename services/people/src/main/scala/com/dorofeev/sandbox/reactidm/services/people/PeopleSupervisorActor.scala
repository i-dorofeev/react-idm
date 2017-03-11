package com.dorofeev.sandbox.reactidm.services.people

import akka.actor.Actor
import akka.actor.Actor.Receive

object PeopleSupervisorActorCommands {
  case class DispatchCommand(id: String, command: Any)
}

class PeopleSupervisorActor extends Actor {

  override def receive: Receive = ???
}
