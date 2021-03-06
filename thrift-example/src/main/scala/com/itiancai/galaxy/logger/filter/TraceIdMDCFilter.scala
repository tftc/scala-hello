package com.itiancai.galaxy.logger.filter

import com.twitter.finagle.tracing.Trace
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.util.Future
import org.slf4j.MDC
import org.springframework.stereotype.Component

@Component
class TraceIdMDCFilter[Req, Rep] extends SimpleFilter[Req, Rep]{

  override def apply(request: Req, service: Service[Req, Rep]): Future[Rep] = {
    val traceId = Trace.id.traceId.toString()
    MDC.put("traceId", traceId)
    service(request)
  }
}
