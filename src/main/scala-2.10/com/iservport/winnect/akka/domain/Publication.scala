package com.iservport.winnect.akka.domain

import java.util.Date

import org.joda.time.DateTime


/**
 * Classe que representa uma publicação.
 *
 * Created by mauriciofernandesdecastro on 08/10/14.
 */
case class Publication() {

  var id: String = ""
  var pSourceDesc: String = ""
  var pSourceName: String = ""
  var pSourceRoot: String = ""
  var pSourceCode: String = ""
  var pType: Integer = 0
  var pNumber: String = ""
  var pSubject: String = ""
  var pIssued: String = ""
  var dIssued: Date = new Date()
  var pIssuedTime: String = ""
  var dLimit: Date = new Date()
  var pLimitTime: String = ""
  var dDecision: Date = new Date()
  var pDecisionTime: String = ""
  var pLocation: String = ""

  val test = ""
  val datePattern = ".+(\\d{2})/(\\d{2})/(\\d{2,4}) (.*),(.*)".r
  val numberPattern = ".+Nº (.*)".r

  def pId = s"$pSourceCode:$pType:$pNumber"

  def add(field: PublicationField) = {
    val modePattern = "(.*)_(.)".r
    field.fieldType match {
      case "HEADER" => {
        pSourceName = field.fragment.trim + " "
        pSourceDesc += pSourceName
        if (pSourceRoot.isEmpty) pSourceRoot = pSourceName
      }
      case "UASG" => {
        pSourceCode = field.fragment.trim
      }
      case modePattern("MODE", modeType) => {
        pType = modeType.toInt
        field.fragment match {
          case numberPattern(number) => pNumber = number
          case _ => pNumber = field.fragment
        }

      }
      case "SUBJECT" => pSubject = field.fragment.trim
      // match issue date and time
      case "ISSUED" => {
        field.fragment.trim match {
          // match date
          case datePattern(day, month, year, time) => {
            dIssued = new DateTime(year.toInt, month.toInt, day.toInt, 0, 0).toDate
            pIssuedTime = time
          }
          case _ => dIssued = new Date()
        }
      }
      case "LIMIT_DATE" => {
        field.fragment.trim match {
          // match date
          case datePattern(day, month, year, time) => {
            dLimit = new DateTime(year.toInt, month.toInt, day.toInt, 0, 0).toDate
            pLimitTime = time
          }
          case _ => dIssued = new Date()
        }
      }
      case "DECISION_DATE" => {
        field.fragment.trim match {
          // match date
          case datePattern(day, month, year, time, location) => {
            dDecision = new DateTime(year.toInt, month.toInt, day.toInt, 0, 0).toDate
            pDecisionTime = time
            pLocation = location
          }
          case _ => dIssued = new Date()
        }
      }
      case _ =>
    }
  }

  override def toString: String = s"""
  $pId
  $pSourceName
  [dIssued  =$dIssued] $pIssuedTime
  [dLimit   =$dLimit] $pLimitTime
  [dDecision=$dDecision] $pDecisionTime
  $pLocation
  """

}



