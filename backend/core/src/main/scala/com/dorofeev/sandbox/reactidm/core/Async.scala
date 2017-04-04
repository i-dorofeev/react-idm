package com.dorofeev.sandbox.reactidm.core

import akka.actor.Stash

import scala.concurrent.Future
import scala.util.Success

trait Async extends Stash {

  import context.dispatcher

  def async[A](future: => Future[A]): Future[A] = {
    context become stashing
    future andThen {
      case Success(_) =>
        context.unbecome()
        unstashAll
    }
  }

  private def stashing: Receive = {
    case _ => stash()
  }

}
