package com.itiancai.passport.thrift

import com.itiancai.galaxy.logger.filter.LoggingMDCFilter
import com.itiancai.passport.thrift.PassportService.Login
import com.itiancai.passport.thrift.Source.Web
import com.twitter.finagle.{Service, SimpleFilter, Thrift}
import com.twitter.util.Future._
import com.twitter.util.{Await, Future}
import org.junit.runner.RunWith
import org.scalatest.WordSpec
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class TestClient extends WordSpec{

  "be allowed" in {
    //client 1
    val client = Thrift.newIface[PassportService.FutureIface]("localhost:9999")//123.57.227.107

    val userLogin = UserLogin.apply("aaa","1123123",Web)

    val result1: Future[PassportResult] = client.login(userLogin)

    println("aaa" + Await.result(result1, DEFAULT_TIMEOUT).errMsg)

    client.login(userLogin).onSuccess{
      response => println("aaa2" + response.errMsg)
    }             .onFailure{
      exception=>println("a3"+exception)
    }


    val clientServiceIface: PassportService.ServiceIface
    = Thrift.newServiceIface[PassportService.ServiceIface]("localhost:9999")

    val result2: Future[Login.Result] = clientServiceIface.login(Login.Args(userLogin))
    //#thriftclientapi-call

    println("aaa3" + Await.result(result2).success.get.errMsg)


    //
    val uppercaseFilter = new SimpleFilter[Login.Args, Login.Result] {
      def apply(req: Login.Args, service: Service[Login.Args, Login.Result]): Future[Login.Result] = {
        //val uppercaseRequest = req.copy(message = req.toUpperCase)
        service(req)
      }
    }
    val mdcFilter = new LoggingMDCFilter[Login.Args,Login.Result]
    mdcFilter andThen uppercaseFilter andThen clientServiceIface.login
    //  //uppercaseFilter andThen clientServiceIface.login
  }



}
