package com.dorofeev.sandbox.reactidm.services.people

import java.util.UUID

import akka.actor.{Actor, ActorRef, Props}

abstract class ManagerCommands
  case class CreatePerson(firstName: String, lastName: String) extends ManagerCommands

class ManagerActor(val peopleSupervisor: ActorRef) extends Actor {

  override def receive = {
    case msg => println("ManagerActor: " + msg)

    case CreatePerson(firstName, lastName) => {

      val creator = context.actorOf(Props(new PersonCreatorActor(sender(), peopleSupervisor, firstName, lastName)))
      creator ! PersonCreatorActorCommands.Create


    }

  }
}
