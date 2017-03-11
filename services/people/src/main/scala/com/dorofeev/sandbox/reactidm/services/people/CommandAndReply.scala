package com.dorofeev.sandbox.reactidm.services.people

abstract case class Command(id: String)
abstract case class CommandReply(id: String)
case class SuccessReply(override val id: String) extends CommandReply(id)
case class FailureReply(override val id: String, ex: Throwable) extends CommandReply(id)
