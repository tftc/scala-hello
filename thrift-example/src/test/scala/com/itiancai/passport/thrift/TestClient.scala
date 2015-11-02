package com.itiancai.passport.thrift

import com.itiancai.passport.thrift.Source.Web
import com.twitter.finagle.Thrift
import com.twitter.util.Future._
import com.twitter.util.{Await, Future}


object TestClient extends App {

  val clientServiceIface: PassportService.FutureIface =
//    Thrift.newIface[PassportService.FutureIface]("123.57.227.107:9999")
   Thrift.newIface[PassportService.FutureIface]("localhost:9999")
  val userLogin = UserLogin.apply("aaa","1123123",Web)
  val result: Future[PassportResult] = clientServiceIface.login(userLogin)

  println("aaa" + Await.result(result, DEFAULT_TIMEOUT).errMsg)

}
