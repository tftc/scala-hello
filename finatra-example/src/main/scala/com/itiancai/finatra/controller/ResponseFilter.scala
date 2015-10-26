package com.itiancai.finatra.controller

import com.twitter.finagle.httpx.{Request, Response}
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.finatra.http.HttpHeaders
import com.twitter.util.Future


class ResponseFilter[R <: Request] extends SimpleFilter[R, Response]{

  private def setResponseHeaders(response: Response) = {
    HttpHeaders.set(response, HttpHeaders.Server, "cube")
  }

  override def apply(request: R, service: Service[R, Response]): Future[Response] = {
    for (response <- service(request)) yield {
      setResponseHeaders(response)
      response
    }
  }
}
