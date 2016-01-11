package com.iservport.winnect.akka

import akka.actor.ActorSystem
import com.datastax.driver.core.Cluster

/**
 * Configuração Cassandra.
 *
 * Originalmente publicado em https://github.com/eigengo/activator-akka-cassandra
 */
trait CassandraCluster {
  def cluster: Cluster
}

trait ConfigCassandraCluster extends CassandraCluster {
  def system: ActorSystem

//  private def config = system.settings.config
//
//  import scala.collection.JavaConversions._
//  private val cassandraConfig = config.getConfig("cassandra")
//  private val port = cassandraConfig.getInt("port")
//  private val hosts = cassandraConfig.getStringList("hosts").toList

  lazy val cluster: Cluster =
    Cluster.builder().
      addContactPoints(hosts: _*).
//      withCompression(ProtocolOptions.Compression.SNAPPY).
      withPort(port).
      build()
}