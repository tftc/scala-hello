package com.itiancai.galaxy.inject

import com.twitter.inject.Logging
import org.springframework.context.support.GenericApplicationContext

object InstalledModules {

  def create(injector: Injector, modules: Seq[Module]): InstalledModules = {
    modules foreach configModules
    new InstalledModules(injector, modules)
  }

  private def configModules(module: Module) = {
    module.config()
  }
}


case class InstalledModules(injector: Injector, modules: Seq[Module]) extends Logging {

  def postStartup() {
    modules foreach {
      case injectModule: ModuleLifeCycle =>
        try {
          injectModule.singletonStartup(injector)
        } catch {
          case e: Throwable =>
            error("Startup method error in " + injectModule, e)
            throw e
        }
      case _ =>
    }
  }

  // Note: We don't rethrow so that all modules have a change to shutdown
  def shutdown() {
    modules foreach {
      case injectModule: ModuleLifeCycle =>
        try {
          injectModule.singletonShutdown(injector)
        } catch {
          case e: Throwable =>
            error("Shutdown method error in " + injectModule, e)
        }
      case _ =>
    }
  }
}