package com.itiancai.passport.thrift

import com.twitter.util.Future
import com.itiancai.dao.ExecuteDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class PassportServiceImpl extends PassportService.FutureIface{


  @Autowired
  var  excu:ExecuteDao = null;

  override def hi(msg: String): Future[String] = {

    println(s" Server received: '$msg'")
    excu.msg2(msg);
    Future.value(s"hi $msg")
  }
}
