package com.dorofeev.sandbox.reactidm.services.people

import akka.actor.{ActorSystem, Props}

object Service extends App {

  val system = ActorSystem("people")

  val peopleSupervisor = system.actorOf(Props(new PeopleSupervisorActor()), "peopleSupervisor")
  val manager = system.actorOf(Props(new ManagerActor(peopleSupervisor)), "manager")

  println(manager.path)
  manager ! "hello, manager!"

}
