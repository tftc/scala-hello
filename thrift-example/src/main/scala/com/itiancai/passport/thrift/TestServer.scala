package com.itiancai.passport.thrift

import com.twitter.finagle.Thrift
import com.twitter.util.Await
import com.itiancai.bootstrap.SpringBoot
import org.springframework.context.annotation.AnnotationConfigApplicationContext


object TestServer extends App{


    val context:AnnotationConfigApplicationContext = SpringBoot.context

    val passService:PassportServiceImpl = context.getBean(classOf[PassportServiceImpl])

    val service = Thrift.serveIface("0.0.0.0:9999", passService)
    Await.ready(service)

}
