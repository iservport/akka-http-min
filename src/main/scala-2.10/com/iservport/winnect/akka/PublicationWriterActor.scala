package com.iservport.winnect.akka

import akka.actor.Actor
import com.datastax.driver.core.Cluster
import com.iservport.winnect.akka.domain.{Publication, Opportunity}

/**
 * Ator responsável pela escrita de uma publicação.
 *
 * Created by mauriciofernandesdecastro on 24/11/14.
 */
class PublicationWriterActor(cluster: Cluster) extends Actor {

  val session = cluster.connect(Keyspaces.winnect)

  val publicationStatement = session.prepare("INSERT INTO publications(" +
    "id" +
    ", pSourceCode" +
    ", pSourceName" +
    ", pSourceRoot" +
    ", pSourceDesc" +
    ", pType" +
    ", pNumber" +
    ", pSubject" +
    ", dIssued" +
    ", pIssuedTime" +
    ", dLimit" +
    ", pLimitTime" +
    ", dDecision" +
    ", pDecisionTime" +
    ", pLocation) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);")

  val sourceStatement = session.prepare("INSERT INTO sources(" +
    "pSourceCode" +
    ", pSourceName" +
    ", pSourceRoot" +
    ", pSourceDesc" +
    ", dIssued) VALUES (?, ?, ?, ?, ?);")

  val opportunityStatement = session.prepare("INSERT INTO opportunities(" +
    "entityId" +
    ", id" +
    ", pSourceCode" +
    ", pSourceName" +
    ", pSourceRoot" +
    ", pSourceDesc" +
    ", pType" +
    ", pNumber" +
    ", pSubject" +
    ", dIssued" +
    ", pIssuedTime" +
    ", dLimit" +
    ", pLimitTime" +
    ", dDecision" +
    ", pDecisionTime" +
    ", pLocation) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);")

  def savePublication(publication: Publication): Unit = {
    session.executeAsync(publicationStatement.bind(
      publication.pId
      , publication.pSourceCode
      , publication.pSourceName
      , publication.pSourceRoot
      , publication.pSourceDesc
      , publication.pType
      , publication.pNumber
      , publication.pSubject.trim
      , publication.dIssued
      , publication.pIssuedTime
      , publication.dLimit
      , publication.pLimitTime
      , publication.dDecision
      , publication.pDecisionTime
      , publication.pLocation
    ))
  }

  def saveUasg(publication: Publication): Unit = {
    session.executeAsync(sourceStatement.bind(
      publication.pSourceCode
      , publication.pSourceName
      , publication.pSourceRoot
      , publication.pSourceDesc
      , publication.dIssued
    ))
  }

  def saveOpportunity(opportunity: Opportunity): Unit = {
    session.executeAsync(opportunityStatement.bind(
      opportunity.entityId
      , opportunity.id
      , opportunity.pSourceCode
      , opportunity.pSourceName
      , opportunity.pSourceRoot
      , opportunity.pSourceDesc
      , opportunity.pType
      , opportunity.pNumber
      , opportunity.pSubject.trim
      , opportunity.dIssued
      , opportunity.pIssuedTime
      , opportunity.dLimit
      , opportunity.pLimitTime
      , opportunity.dDecision
      , opportunity.pDecisionTime
      , opportunity.pLocation
    ))
  }

  def receive: Receive = {
    case publication: Publication => {
//      println(s"Ready to write ${publication}")
      savePublication(publication)
      saveUasg(publication)
    }
    case opportunity: Opportunity =>
      saveOpportunity(opportunity)
    case other => println(other)
  }

}

