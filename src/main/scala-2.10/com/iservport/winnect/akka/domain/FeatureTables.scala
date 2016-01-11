package com.iservport.winnect.akka.domain

import slick.driver.MySQLDriver.api._
import slick.lifted.{ForeignKeyQuery, ProvenShape}

/**
 * Created by mauriciofernandesdecastro on 30/05/15.
 */

/**
 * Solicitações de atendimento.
 * @param tag
 */
class Orders(tag: Tag)
  extends Table[(Int, Char, Int, Int, String)](tag, "ord_abstract") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def resolution = column[Char]("resolution")
  def entityId = column[Int]("entityId")
  def partId = column[Int]("partId")
  def checkInData = column[String]("checkInData")
  def * : ProvenShape[(Int, Char,Int,Int,String)] =
    (id,resolution,entityId,partId,checkInData)
}

/*
`type` char(1) NOT NULL,
--`id` int(11) NOT NULL AUTO_INCREMENT,
`issueDate` datetime DEFAULT NULL,
`privacyLevel` char(1) NOT NULL,
`version` int(11) DEFAULT NULL,
--`resolution` char(1) NOT NULL,
`internalNumber` bigint(20) NOT NULL,
`checkOutTime` datetime DEFAULT NULL,
--`entityId` bigint(20) DEFAULT NULL,
`ownerId` bigint(20) DEFAULT NULL,
--`partId` int(11) DEFAULT NULL,
`faceValue` decimal(19,2) DEFAULT NULL,
`categoryId` int(11) DEFAULT '262',
`remarks` varchar(255) DEFAULT NULL,
`currencyId` int(11) DEFAULT NULL,
`position` char(1) DEFAULT NULL,
--`checkInData` longtext,
`checkOutData` longtext,
`nextCheckDate` datetime DEFAULT NULL
*/

class Features(tag: Tag)
  extends Table[(Int, Int, Int, String, String, Char, Int)](tag, "core_feature") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def version = column[Int]("version")
  def contextId = column[Int]("contextId")
  def featureCode = column[String]("featureCode")
  def featureName = column[String]("featureName")
  def featureType = column[Char]("featureType")
  def osConstraints = column[Int]("osConstraints")
  def * : ProvenShape[(Int, Int, Int, String, String, Char, Int)] =
    (id,version,contextId,featureCode,featureName,featureType,osConstraints)
}

class FeatureDictionaries(tag: Tag)
  extends Table[(Int, Int, Int, Int, Int, String)](tag, "bid_feature") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def version = column[Int]("version")
  def entityId = column[Int]("entityId")
  def featureId = column[Int]("featureId")
  def priority = column[Int]("priority")
  def meaning = column[String]("meaning")
  def * : ProvenShape[(Int, Int, Int, Int, Int, String)] =
    (id,version,entityId,featureId,priority,meaning)
  def feature: ForeignKeyQuery[Features, (Int, Int, Int, String, String, Char, Int)] =
    foreignKey("FEATURE_FK", featureId, TableQuery[Features])(_.id)
}
