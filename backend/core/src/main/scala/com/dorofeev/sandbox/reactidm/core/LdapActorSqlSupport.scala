package com.dorofeev.sandbox.reactidm.core

import slick.jdbc.H2Profile.api._

//noinspection UnitMethodIsParameterless,TypeAnnotation,ScalaFileName
class ConnectorObjects(tag: Tag) extends Table[(String, String)](tag, "connector_objects") {

  def uid = column[String]("uid", O.PrimaryKey)
  def obj = column[String]("object")

  def * = (uid, obj)
}

class Reconciliation(tag: Tag) extends Table[(String, String)](tag, "reconciliation") {

  def uid = column[String]("uid", O.PrimaryKey)
  def reconciliationId = column[String]("reconciliation_id")

  def * = (uid, reconciliationId)
}
