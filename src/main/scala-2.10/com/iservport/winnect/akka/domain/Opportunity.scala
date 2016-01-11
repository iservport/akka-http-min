package com.iservport.winnect.akka.domain

/**
 * Created by mauriciofernandesdecastro on 12/05/15.
 */
case class Opportunity(entityId: String
  , id: String
  , pSourceDesc: String
  , pSourceName: String
  , pSourceRoot: String
  , pSourceCode: String
  , pType: Integer
  , pNumber: String, pSubject: String
  , dIssued: java.util.Date
  , pIssuedTime: String
  , dLimit: java.util.Date
  , pLimitTime: String
  , dDecision: java.util.Date
  , pDecisionTime: String
  , pLocation: String) {

}
