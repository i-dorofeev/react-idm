package com.dorofeev.sandbox.reactidm.core

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Keep, Sink}
import slick.dbio.{DBIOAction, Effect, NoStream}
import slick.jdbc.H2Profile.api._

object SlickStreaming {

  def dbSink[A, R, S <: NoStream, E <: Effect](dbAction: A => DBIOAction[R,S,E], batchSize: Long)(implicit db: Database): Sink[A, NotUsed] = {
    Flow[A]
      .map(dbAction(_))
      .batch(batchSize, firstAction => List(firstAction)) { (batch, action) => action :: batch }
      .map(DBIO.sequence(_))
      .mapAsync(1)(db.run(_))
      .toMat(Sink.ignore)(Keep.none)
  }

}
