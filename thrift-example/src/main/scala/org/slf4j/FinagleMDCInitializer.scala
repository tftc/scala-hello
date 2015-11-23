package org.slf4j

import com.itiancai.galaxy.logger.FinagleMDCAdapter

object FinagleMDCInitializer {

  def init() {
    MDC.getMDCAdapter // Make sure default MDC static initializer has run
    MDC.mdcAdapter = new FinagleMDCAdapter // Swap in the Finagle adapter
  }
}
