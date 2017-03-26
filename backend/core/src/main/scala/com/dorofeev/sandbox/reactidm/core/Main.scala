package com.dorofeev.sandbox.reactidm.core

import akka.actor.{ActorSystem, Props}

object Main extends App {

  val system = ActorSystem("core")

  val a = system.actorSelection("user/reconciliationActor")
  val ldapActor = system.actorOf(Props(new LdapActor(a)), "ldapActor")
  val reconciliationActor = system.actorOf(Props(new ReconciliationActor(ldapActor)), "reconciliationActor")

  reconciliationActor ! ReconcileCmd


  abstract class AbstractResourceObject

  case class ResourceObject(id: String, name: String, objectClass: String) extends AbstractResourceObject
  case class ResourceAssociation(subjectId: String, objectId: String) extends AbstractResourceObject

}
