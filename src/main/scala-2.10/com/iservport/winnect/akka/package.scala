package com.iservport.winnect

import com.typesafe.config.ConfigFactory

/**
 * Created by mauriciofernandesdecastro on 02/10/14.
 */
package object akka {

  val conf = ConfigFactory.load() //.withFallback(ConfigFactory.load("application.conf"))

  val cassandraConf = conf.getConfig("akka-cassandra.main.db.cassandra")

  val scheduleConf = conf.getConfig("winnectSchedule")

  import scala.collection.JavaConversions._
  val hosts = cassandraConf.getStringList("hosts").toList

  val port = cassandraConf.getInt("port")

  val sparkConf = conf.getConfig("winnectSpark")

  val sparkMaster = sparkConf.getString("master")

  private[akka] object Keyspaces {
    val winnect = "winnect"
  }

  private[akka] object ColumnFamilies {
    val publications = "publications"
  }

}
