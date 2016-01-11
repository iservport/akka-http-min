package com.iservport.winnect.akka.comprasnet

import java.io.InputStreamReader
import java.text.Normalizer

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.event.LoggingReceive
import com.iservport.winnect.akka.domain.{Publication, PublicationField}
import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
import org.xml.sax.InputSource

import scala.collection.mutable.ListBuffer
import scala.xml.Node
import scala.xml.parsing.NoBindingFactoryAdapter

/**
  * Created by mauriciofernandesdecastro on 11/01/16.
  */
class ComprasnetProcessor(readerActor: ActorRef) extends Actor with ActorLogging {

  lazy val adapter = new NoBindingFactoryAdapter
  lazy val parser = (new SAXFactoryImpl).newSAXParser

  def receive = LoggingReceive {
    case (input: InputStreamReader, pageNumber:Int) =>
      val forms = (adapter.loadXML(new InputSource(input), parser) \\ "form")
      if (forms.length>0) readerActor ! ComprasnetFlow(pageNumber+1)
      for {form <- forms
            tr <- form \\ "tr" if (tr \ "@class").text.contains("tex3")
      } yield self ! tr
    case fragment: Node =>
      log.debug("Starting unmarshall...")
      val p = Publication()
      var defaultFieldType = "HEADER"
      var fields = new ListBuffer[PublicationField]()
      for (detail <- fragment.descendant if detail isAtom) {
        val fieldType = getType(detail.text, defaultFieldType)
        val prefix = Option(typeMap.get(fieldType)) match {
          case Some(value) => value.toString
          case None => ""
        }
        val f = new PublicationField(fieldType, detail.text, prefix)
        if (!f.isEmpty) { fields += f }
        defaultFieldType = fieldType
      }
      for (f <- fields) {
        p.add(f)
      }
      log.debug(s"Ready to write iso-8859-1 <<${p}>>")
      readerActor ! p
    case _ =>
  }

  val typeMap = Map(
    "type.UASG" -> "Codigo da UASG"
  , "type.MODE_1" -> "Convite"
  , "type.MODE_2" -> "Tomada de preco"
  , "type.MODE_3" -> "Concorrencia"
  , "type.MODE_4" -> "Concurso"
  , "type.MODE_5" -> "Pregao Eletronico"
  , "type.MODE_6" -> "Pregao Presencial"
  , "type.MODE_7" -> "RDC Eletronico"
  , "type.MODE_8" -> "RDC Presencial"
  , "type.ISSUED" -> "Edital a partir de"
  , "type.ADDRESS" -> "Endereco"
  , "type.PHONE" -> "Telefone"
  , "type.FAX" -> "Fax"
  , "type.LIMIT_DATE" -> "Entrega da Proposta"
  , "type.DECISION_DATE" -> "Abertura da Proposta"
  )

  /**
    * Método para determinar o tipo de um detalhe.
    *
    * @param detailText
    */
  def getType(detailText:String, defaultType: String) = {
    println(normalize(detailText))
    val typeSeq = for {
      (k, v) <- typeMap if normalize(detailText) contains v.toString
    }
      yield k

    typeSeq.headOption.getOrElse(defaultType)
  }

  def normalize(text: String) = Normalizer.normalize(text, Normalizer.Form.NFKD).replaceAll("[^\\p{ASCII}]", "")
}
