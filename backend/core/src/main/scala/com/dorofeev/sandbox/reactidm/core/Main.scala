package com.dorofeev.sandbox.reactidm.core

import akka.actor.{ActorSystem, Props}

object Main extends App {

  val system = ActorSystem("core")

  val ldapActor = system.actorOf(Props(new LdapActor()), "ldapActor")

  abstract class AbstractResourceObject

  case class MyConnectorObject(id: String, name: String, objectClass: String) extends AbstractResourceObject
  case class ResourceAssociation(subjectId: String, objectId: String) extends AbstractResourceObject

}
