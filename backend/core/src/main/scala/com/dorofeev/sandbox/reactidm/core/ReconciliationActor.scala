package com.dorofeev.sandbox.reactidm.core

import java.util

import akka.actor.{Actor, ActorRef}
import com.dorofeev.sandbox.reactidm.core.Main.ResourceObject

import scala.collection.mutable.ListBuffer

class ReconciliationActor(val resourceActor: ActorRef) extends Actor {

  val resourceObjectList: ListBuffer[ResourceObject] = ListBuffer[ResourceObject]()

  override def receive: Receive = {

    case ReconcileCmd => resourceActor ! ReconcileCmd
    case ReconciliationObject(obj) => resourceObjectList += obj
    case ReconciliationFinished => resourceObjectList.foreach(println)
    case ResourceObjectAddedCreatedOrUpdated(obj) => println("created or updated " + obj)
    case ResourceObjectDeleted(obj) => println("deleted " + obj)

  }
}
