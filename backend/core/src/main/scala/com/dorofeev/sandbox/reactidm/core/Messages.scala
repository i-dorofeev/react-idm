package com.dorofeev.sandbox.reactidm.core

import com.dorofeev.sandbox.reactidm.core.Main.ResourceObject

case class ReconcileCmd()
case class ReconciliationObject(resourceObject: ResourceObject)
case class ReconciliationFinished()
case class ResourceObjectAddedCreatedOrUpdated(resourceObject: ResourceObject)
case class ResourceObjectDeleted(resourceObject: ResourceObject)
