package com.itiancai.galaxy.logger.filter

import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.util.Future
import org.slf4j.{FinagleMDCInitializer, MDC}
import org.springframework.stereotype.Component

/**
 * Initialize the Logback Mapped Diagnostic Context, and clear the MDC after each request
 * @see http://logback.qos.ch/manual/mdc.html
 */
@Component
class LoggingMDCFilter[Req, Rep] extends SimpleFilter[Req, Rep] {

  /* Initialize Finagle MDC adapter which overrides the standard Logback one */
  FinagleMDCInitializer.init()

  override def apply(request: Req, service: Service[Req, Rep]): Future[Rep] = {
    service(request).ensure {
      MDC.clear()
    }

  }
}
