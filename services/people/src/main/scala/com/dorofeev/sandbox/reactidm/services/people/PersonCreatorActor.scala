package com.dorofeev.sandbox.reactidm.services.people

import java.util.UUID

import akka.actor.{Actor, ActorRef, Props}
import com.dorofeev.sandbox.reactidm.services.people.PeopleSupervisorActorCommands.DispatchCommand

object PersonCreatorActorCommands {
  case class Create()
}

class PersonCreatorActor(
        val requester: ActorRef,
        val peopleSupervisor: ActorRef,
        val firstName: String,
        val lastName: String) extends Actor {

  override def receive: Receive = {
    case PersonCreatorActorCommands.Create => {
      val id = UUID.randomUUID().toString
      peopleSupervisor ! DispatchCommand(id, Create(firstName, lastName))
      val person = context.child(id) match {
        case Some(child) => child
        case None => context.actorOf(Props(classOf[PersonActor]), id)
      }

      person ! Create(firstName, lastName)
    }
  }
}
