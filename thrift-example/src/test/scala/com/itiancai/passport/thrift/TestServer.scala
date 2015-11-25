package com.itiancai.passport.thrift

import com.itiancai.galaxy.inject.ContextConfig
import com.itiancai.galaxy.logger.filter.{LoggingMDCFilter, TraceIdMDCFilter}
import com.itiancai.galaxy.thrift.codegen.MethodFilters
import com.itiancai.galaxy.thrift.{ThriftRequest, ThriftRouter, ThriftServer}
import com.itiancai.passport.SpringBoot
import com.itiancai.passport.thrift.PassportService.{Login, Regist, RegisterValidate, UserInfo}
import com.twitter.finagle.Service
import com.twitter.util.Future


object TestServer extends ThriftServer{

  override protected def configureThrift(router: ThriftRouter): Unit = {
    router.filter[LoggingMDCFilter[ThriftRequest,Any]]
    .filter[TraceIdMDCFilter[ThriftRequest, Any]]
    .add[PassportServiceImpl](PassporServiceFilterCreator.create)
  }

  override  protected def configureSpring(): ContextConfig = {
    new ContextConfig {
      override def registerClass(): Seq[Class[_]] = {
        Seq(classOf[SpringBoot])
      }

      override def scanPackageName(): Seq[String] = {
        Seq("com.itiancai.passport")
      }
    }
  }
}

object PassporServiceFilterCreator {
  def create(filters: MethodFilters, underlying: PassportService[Future]) = {
    new PassportService[Future] {
      override def login(user: UserLogin): Future[PassportResult] = filters.create(Login)(Service.mk(underlying.login))(user)

      override def userInfo(userId: Long): Future[PassportResult] = filters.create(UserInfo)(Service.mk(underlying.userInfo))(userId)

      override def registerValidate(name: String, value: String): Future[PassportResult] =
        filters.create(RegisterValidate)(Service.mk((underlying.registerValidate _).tupled))(name,value)

      override def regist(user: User): Future[PassportResult] = filters.create(Regist)(Service.mk(underlying.regist))(user)
    }
  }
}




//object FinagleExample extends  SpringConfig{
//
//  override  def scanPackageName(): Seq[String] = {Seq("com.itiancai")}
//
//  override  def registerClass(): Seq[Class[_]] = { Seq(classOf[SpringBoot])};
//}

//  override def postStartup(): Unit ={
//    //val passService: PassportServiceImpl = context.getBean(classOf[PassportServiceImpl])
//
//
//    def logMdcFilter = new LoggingMDCFilter[Array[Byte], Array[Byte]]
//
//    def traceIdFilter = new TraceIdMDCFilter[Array[Byte], Array[Byte]]
//
//
//    val service = new PassportService$FinagleService(passService,Thrift.protocolFactory,
//      ServerStatsReceiver.scope("thrift"),Thrift.maxThriftBufferSize)
//
//    val filter = logMdcFilter andThen traceIdFilter andThen service
//
//    val server2 =  Thrift.serve("0.0.0.0:9999", filter);
//
//    Await.ready(server2)
//  }