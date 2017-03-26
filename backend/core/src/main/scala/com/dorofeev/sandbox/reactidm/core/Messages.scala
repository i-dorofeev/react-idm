package com.dorofeev.sandbox.reactidm.core

import com.dorofeev.sandbox.reactidm.core.Main.MyConnectorObject

case class ReconcileCmd()
case class ReconciliationObject(resourceObject: MyConnectorObject)
case class ReconciliationFinished()
case class ResourceObjectAddedCreatedOrUpdated(resourceObject: MyConnectorObject)
case class ResourceObjectDeleted(resourceObject: MyConnectorObject)
