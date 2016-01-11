package com.iservport.winnect.akka

import java.io.{ByteArrayInputStream, InputStreamReader}

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by mauriciofernandesdecastro on 11/01/16.
  */
abstract class AbstractFlow(val pageNumber: Int = 1) {

  implicit val system = ActorSystem("winnect")
  implicit val ec: ExecutionContext = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val host = ""

  val encoding = "ISO-8859-1"

  val request:HttpRequest = null

  /**
    * Actual page reader.
    */
  def page: Future[(InputStreamReader, Int)] = {
    Source.single(request).via(Http().outgoingConnection(host)).runWith(Sink.head).flatMap( response =>
      response.entity.toStrict(FiniteDuration(120, "seconds")) collect {
        case entity =>
          (new InputStreamReader(
            new ByteArrayInputStream(entity.data.decodeString(encoding).getBytes)), pageNumber)
      }
    )
  }


}
