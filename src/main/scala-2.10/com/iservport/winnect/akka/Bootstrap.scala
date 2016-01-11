package com.iservport.winnect.akka


import akka.actor.{ActorSystem, Props}
import akka.kernel.Bootable
import com.datastax.driver.core.Cluster
import com.iservport.winnect.akka.comprasnet.ComprasnetFlow

/**
 * Inicia o microservice (microkernel Akka).
 *
 * Created by mauriciofernandesdecastro on 09/01/16.
 */
class Bootstrap extends Bootable {

  lazy val cluster: Cluster = Cluster.builder().addContactPoints("192.168.10.5").withPort(9042).build()

  val system = ActorSystem("winnect")
  lazy val publicationWriterActor = system.actorOf(Props(new PublicationWriterActor(cluster)), "actWriter")
  lazy val readerActor = system.actorOf(Props(new ReaderActor(publicationWriterActor)), "actReader")


  override def startup(): Unit = {
    readerActor ! ComprasnetFlow(1)
  }

  def shutdown = {
    println("Winnect root shuting down")
    system.shutdown()
    system.awaitTermination()
  }

}
