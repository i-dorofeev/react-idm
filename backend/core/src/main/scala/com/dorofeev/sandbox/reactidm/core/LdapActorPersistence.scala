package com.dorofeev.sandbox.reactidm.core

import scala.concurrent.Future

trait LdapActorPersistence {

  def restore(uid: SAttributeUid): Future[Option[SConnectorObject]]
  def persist(connectorObject: SConnectorObject): Future[Unit]
  def update(connectorObject: SConnectorObject): Future[Unit]
  def delete(uid: SAttributeUid): Future[Unit]
}
