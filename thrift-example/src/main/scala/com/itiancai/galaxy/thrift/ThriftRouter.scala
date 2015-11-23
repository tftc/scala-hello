package com.itiancai.galaxy.thrift


import com.itiancai.galaxy.inject.Injector
import com.itiancai.galaxy.thrift.codegen.MethodFilters
import com.twitter.finagle.Filter
import com.twitter.finagle.stats.StatsReceiver
import com.twitter.scrooge.ThriftService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class ThriftRouter @Autowired()(injector: Injector, statsReceiver: StatsReceiver) {


  //private val injector: Injector = _


  private type ThriftFilter = Filter[ThriftRequest, Any, ThriftRequest, Any]

  private[galaxy] var filterChain: ThriftFilter = Filter.identity

  private[galaxy] var name: String = ""

  private var done = false

  private[galaxy] var filteredService: ThriftService = _


  /** Add global filter used for all requests */
  def filter[FilterType <: ThriftFilter : Manifest] = {
    filterChain = filterChain andThen injector.instance[FilterType]
    this
  }

  def filter(clazz: Class[_ <: ThriftFilter]) = {
    filterChain = filterChain andThen injector.instance(clazz)
    this
  }


  def add[T <: ThriftService : Manifest](filterFactory: (MethodFilters, T) => ThriftService) {
    addFilteredService(filterFactory.apply(createMethodFilters, injector.instance[T]))
  }

  @deprecated("Thrift services should be added with a filter factory.", "since Scrooge 4.x")
  def addUnfiltered[T <: ThriftService : Manifest] = {
    addFilteredService(injector.instance[T])
    this
  }

  private[galaxy] def serviceName(name: String) = {
    this.name = name
    this
  }

  private def addFilteredService[T <: ThriftService](thriftService: ThriftService): Unit = {
    assert(!done, "ThriftRouter#Add cannot be called multiple times, as we don't currently support serving multiple thrift services.")
    done = true
    filteredService = thriftService
  }

  private def createMethodFilters: MethodFilters = {
    new MethodFilters(statsReceiver.scope(name), filterChain)
  }

}
