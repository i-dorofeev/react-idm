package com.dorofeev.sandbox.reactidm.core
import akka.NotUsed
import akka.stream.scaladsl._
import com.dorofeev.sandbox.reactidm.core.SlickStreaming._
import org.json4s._
import org.json4s.native.Serialization
import org.json4s.native.Serialization.{read, write}
import slick.jdbc.H2Profile
import slick.jdbc.H2Profile.api._
import slick.jdbc.meta.MTable
import slick.lifted.TableQuery

import scala.concurrent.Future


class H2LdapActorPersistence extends LdapActorPersistence {

  import scala.concurrent.ExecutionContext.Implicits.global

  private val connectorObjects: TableQuery[ConnectorObjects] = TableQuery[ConnectorObjects]
  private val reconciliation: TableQuery[Reconciliation] = TableQuery[Reconciliation]

  private val db: H2Profile.backend.Database = Database.forConfig("db")

  private implicit val formats = Serialization.formats(ShortTypeHints(
    List(classOf[SAttributeUid], classOf[SAttributeName], classOf[SAttributeString], classOf[SAttributeInt])))

  def setupDb: Future[Unit] = {
    val tables = List(connectorObjects, reconciliation)

    val existingTables = db.run(MTable.getTables)

    val ddlStatements = existingTables.map(t => {
      val names = t.map(mt => mt.name.name)
      tables.filter(table =>
        !names.contains(table.baseTableRow.tableName)).map(_.schema.create)
    })

    ddlStatements.map(statements => db.run(DBIO.sequence(statements)))
  }

  override def restore(uid: SAttributeUid): Future[Option[SConnectorObject]] = {
    db.run(connectorObjects.filter(_.uid === uid.value).result.headOption).map {
        case Some((_, objJson)) => Some(read[SConnectorObject](objJson))
        case None => None
      }
  }

  def reconciliationSink(reconciliationId: String): Sink[SConnectorObject, NotUsed] = {
    Flow[SConnectorObject]
      .map { connectorObject => (connectorObject.uid.value, reconciliationId) }
      .toMat(dbSink(reconciliation.insertOrUpdate(_), 10))(Keep.none)
  }

  override def persist(connectorObject: SConnectorObject): Future[Unit] = {
    db.run(
      connectorObjects += (connectorObject.uid.value, write(connectorObject)))
    .map(_ => None)
  }

  override def update(connectorObject: SConnectorObject): Future[Unit] = {
    db.run(
      (for { co <- connectorObjects if co.uid === connectorObject.uid.value } yield co.obj)
        .update(write(connectorObject)))
    .map(_ => None)
  }

  override def delete(uid: SAttributeUid): Future[Unit] = {
    db.run(
      connectorObjects.filter(_.uid === uid.value).delete)
    .map(_ => None)
  }
}
