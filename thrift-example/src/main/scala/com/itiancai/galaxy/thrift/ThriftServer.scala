package com.itiancai.galaxy.thrift

import com.itiancai.galaxy.inject.modules.StatsReceiverModule
import com.itiancai.galaxy.inject.Module
import com.twitter.finagle.{Thrift, ListeningServer}
import com.twitter.util.{Future, Time, Await}
import grizzled.slf4j.Logging
import com.itiancai.galaxy.inject.{ContextConfig, App}
import com.twitter.conversions.time._



abstract class ThriftServer extends App with Logging{


  private val thriftPortFlag = flag("thrift.port", ":9999", "External Thrift server port")
  private val thriftShutdownTimeout = flag("thrift.shutdown.time", 1.minute, "Maximum amount of time to wait for pending requests to complete on shutdown")

  addFrameworkModule(statsModule)

  protected def statsModule: Module = StatsReceiverModule


  protected def waitForServer() {
    Await.ready(thriftServer)
  }

  /* Overrides */

  override final def main() {
    super.main() // Call GuiceApp.main() to create injector

    info("Startup complete, server ready.")
    waitForServer()
  }


  /* Private Mutable State */
  private var thriftServer: ListeningServer = _


  protected def configureThrift(router: ThriftRouter)


  protected def configureSpring(): ContextConfig

  /* Lifecycle */

  override def postWarmup() {
    super.postWarmup()

    val router = injector.instance[ThriftRouter]
    router.serviceName(name)
    configureThrift(router)
    thriftServer = Thrift.serveIface(thriftPortFlag(), router.filteredService)
    onExit {
      Await.result(
        close(thriftServer, thriftShutdownTimeout().fromNow))
    }
   // info("Thrift server started on port: " + thriftPort.get)
  }


  /* Private */

  private def close(server: ListeningServer, deadline: Time) = {
    if (server != null)
      server.close(deadline)
    else
      Future.Unit
  }

}
