package com.iservport.winnect.akka.comprasnet

import java.net.URLEncoder

import akka.http.scaladsl.model.HttpCharsets._
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.model.{ContentType, HttpEntity, HttpRequest, RequestEntity}
import akka.util.ByteString
import com.iservport.winnect.akka.AbstractFlow
import org.joda.time.DateTime

/**
  * Created by mauriciofernandesdecastro on 10/01/16.
  */
case class ComprasnetFlow(override val pageNumber:Int) extends AbstractFlow {

  override val host = "comprasnet.gov.br"

  val dt_publ_fim = DateTime.now

  val dt_publ_ini = dt_publ_fim.minusDays(7)

  val data = ByteString(
    s"""numprp=
        |&dt_publ_ini=${URLEncoder.encode(dt_publ_ini.toString("dd/MM/yyyy"), encoding)}
        |&dt_publ_fim=${URLEncoder.encode(dt_publ_fim.toString("dd/MM/yyyy"), encoding)}
        |&txtObjeto=
        |&chkModalidade=1&chkModalidade=2&chkModalidade=3&chkModalidade=20&chkModalidade=5&chkModalidade=99&chkTodos=-1
        |&chk_concor=31&chk_concor=32&chk_concor=41&chk_concor=42&chk_concorTodos=-1
        |&chk_pregao=1&chk_pregao=2&chk_pregao=3&chk_pregao=4&chk_pregaoTodos=-1
        |&chk_rdc=1&chk_rdc=2&chk_rdc=3&chk_rdc=4&chk_rdcTodos=-1
        |&optTpPesqMat=M&optTpPesqServ=S
        |&txtlstUasg=
        |&txtlstUf=
        |&txtlstMunicipio=
        |&txtlstModalidade=
        |&txtlstTpPregao=
        |&txtlstConcorrencia=
        |&txtlstGrpMaterial=
        |&txtlstClasMaterial=
        |&txtlstMaterial=
        |&txtlstGrpServico=
        |&txtlstServico=
        |&Origem=F
        |&numpag=$pageNumber
        |""".stripMargin)


  val requestEntity:RequestEntity = HttpEntity(ContentType(`application/x-www-form-urlencoded`, `UTF-8`), data)

  override val request:HttpRequest = HttpRequest().withUri("/ConsultaLicitacoes/ConsLicitacao_Relacao.asp").withMethod(POST).withEntity(requestEntity)

}
