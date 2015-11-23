package com.itiancai.galaxy.inject.modules

import com.itiancai.galaxy.inject.{ContextHolder, Binder, AbstractModule, Injector}

object InjectorModule extends AbstractModule {

  override protected def configure(binder: Binder) = {
    binder bind ContextHolder.injector
  }

}
