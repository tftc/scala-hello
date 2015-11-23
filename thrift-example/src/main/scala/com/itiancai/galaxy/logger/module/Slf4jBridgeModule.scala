package com.itiancai.galaxy.logger.module

import com.itiancai.galaxy.inject.{AbstractModule, Module, Injector}
import org.slf4j.bridge.SLF4JBridgeHandler
import org.springframework.beans.factory.config.ConfigurableBeanFactory

object Slf4jBridgeModule extends AbstractModule {
  override def singletonStartup(injector: Injector) {
    SLF4JBridgeHandler.removeHandlersForRootLogger()
    SLF4JBridgeHandler.install()
  }

}
