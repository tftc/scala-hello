package com.itiancai.galaxy.inject


trait ModuleLifeCycle {

  protected[inject] def singletonStartup(injector: Injector) {}

  protected[inject] def singletonShutdown(injector: Injector) {}

}
