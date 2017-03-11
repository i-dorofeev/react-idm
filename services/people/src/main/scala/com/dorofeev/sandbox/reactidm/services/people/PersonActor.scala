package com.dorofeev.sandbox.reactidm.services.people

import akka.actor.ActorRef
import com.dorofeev.sandbox.reactidm.services.people.Person.Commands.{Create, PersonCommand}
import com.dorofeev.sandbox.reactidm.services.people.Person.Events.{Created, PersonEvent}
import com.rbmhtechnology.eventuate.EventsourcedActor

import scala.util.{Failure, Success}

object Person {

  object Commands {
    abstract case class PersonCommand(override val id: String) extends Command(id)
    case class Create(override val id: String, firstName: String, lastName: String) extends PersonCommand(id)
  }

  object Events {
    abstract class PersonEvent
    case class Created(firstName: String, lastName: String) extends PersonEvent
  }
}


case class PersonAlreadyExistsException(message: String = "", cause: Throwable = null) extends Exception

class PersonState(var firstName: String, var lastName: String) {

}

class PersonActor(
        override val id: String,
        override val aggregateId: Option[String],
        override val eventLog: ActorRef) extends EventsourcedActor {

  var state: PersonState = _

  override def onCommand: Receive = {

    case cmd:Person.Commands.PersonCommand =>
      try {
        val event = onPersonCommand(cmd)
        persist(event) {
          case Success(_) => sender() ! SuccessReply(cmd.id)
          case Failure(err) => sender() ! FailureReply(cmd.id, err)
        }
      } catch {
        case ex: Exception => sender() ! FailureReply(cmd.id, ex)
      }
  }

  private def onPersonCommand(cmd: PersonCommand): PersonEvent = {
    case cmd:Person.Commands.Create => create(cmd)
  }

  private def create(cmd: Create): PersonEvent = {
    if (state != null)
      throw PersonAlreadyExistsException()
    else
      Created(cmd.firstName, cmd.lastName)
  }

  override def onEvent: Receive = {
    case evt: PersonEvent => onPersonEvent(evt)
  }

  private def onPersonEvent(evt: PersonEvent) = {
    evt match {
      case e:Created =>
        state = new PersonState(e.firstName, e.lastName)
    }
  }
}
