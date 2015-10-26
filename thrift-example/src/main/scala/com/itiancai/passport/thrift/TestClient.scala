package com.itiancai.passport.thrift

import com.twitter.finagle.Thrift
import com.twitter.util.Future._
import com.twitter.util.{Await, Future}


object TestClient extends App{

  val clientServiceIface : PassportService.FutureIface =
    Thrift.newIface[PassportService.FutureIface]("localhost:9999")

  val result: Future[String] = clientServiceIface.hi("11111111")

  println("aaa" + Await.result(result, DEFAULT_TIMEOUT))

}
