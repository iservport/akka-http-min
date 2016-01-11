package com.iservport.winnect.akka

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.event.LoggingReceive
import akka.stream.ActorMaterializer
import akka.pattern.pipe
import com.iservport.winnect.akka.comprasnet.{ComprasnetProcessor, ComprasnetFlow}
import com.iservport.winnect.akka.domain.Publication

import scala.concurrent.ExecutionContext

/**
  * Created by mauriciofernandesdecastro on 09/01/16.
  */
class ReaderActor(publicationWriterActor: ActorRef) extends Actor with ActorLogging {

  implicit val system = context.system
  implicit val ec: ExecutionContext = context.dispatcher
  implicit val materializer = ActorMaterializer()

  lazy val comprasnetProcessor = system.actorOf(Props(new ComprasnetProcessor(self)), "comprasnet")

  /**
    *
    * @return
    */
  def receive = LoggingReceive {
    case flow: ComprasnetFlow =>
      flow.page pipeTo comprasnetProcessor
    case p:Publication =>
      publicationWriterActor ! p
    case _ =>
  }

}
