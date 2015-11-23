package com.itiancai.galaxy.thrift.internal

import com.itiancai.galaxy.thrift.ThriftRequest
import com.twitter.finagle.{Filter, Service}
import com.twitter.util.Future

class ThriftRequestUnwrapFilter[T, U]
  extends Filter[ThriftRequest, U, T, U] {

  override def apply(request: ThriftRequest, service: Service[T, U]): Future[U] = {
    service(
      request.args.asInstanceOf[T])
  }
}