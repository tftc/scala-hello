package com.itiancai.galaxy.inject.modules

import com.itiancai.galaxy.inject.{Binder, AbstractModule}
import com.twitter.finagle.stats.LoadedStatsReceiver


object StatsReceiverModule extends AbstractModule {
  override protected def configure(binder: Binder) {
    binder bind LoadedStatsReceiver
  }
}
