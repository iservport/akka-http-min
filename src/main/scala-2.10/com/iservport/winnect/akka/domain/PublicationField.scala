package com.iservport.winnect.akka.domain

/**
 * Created by mauriciofernandesdecastro on 12/01/15.
 */
class PublicationField(val fieldType: String, detailText: String, prefix: String) {

  var modeType = ""

  val fragment:String = detailText match {
    case mode if mode contains "MODE_" => {
      modeType = mode.stripPrefix("MODE_")
      detailText.stripPrefix(prefix+" Nº ").stripPrefix(":")
    }
    case mode if detailText.isEmpty => ""
    case mode => {
      detailText.stripPrefix(prefix).stripPrefix(":")
    }
  }

  def isEmpty: Boolean = (fragment.isEmpty)

}
