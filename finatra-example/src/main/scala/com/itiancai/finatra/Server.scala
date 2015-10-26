package com.itiancai.finatra

import com.itiancai.finatra.controller.{ExampleController, ResponseFilter}
import com.twitter.finagle.httpx.{Request, Response}
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.CommonFilters
import com.twitter.finatra.http.routing.HttpRouter
import com.twitter.finatra.logging.filter.{LoggingMDCFilter, TraceIdMDCFilter}
import com.twitter.finatra.logging.modules.Slf4jBridgeModule

object Server extends HttpServer {
  override def modules = Seq(Slf4jBridgeModule)

  override def configureHttp(router: HttpRouter) {
    router
      .filter[LoggingMDCFilter[Request, Response]]
      .filter[TraceIdMDCFilter[Request, Response]]
      .filter[ResponseFilter[Request]]
      .filter[CommonFilters]
      .add[ExampleController]
  }

}