package com.itiancai.passport.thrift

import com.twitter.util.Future
import com.itiancai.dao.ExecuteDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class PassportServiceImpl extends PassportService.FutureIface {


  @Autowired
  var excu: ExecuteDao = null;

  //  override def hi(msg: String): Future[String] = {
  //
  //    println(s" Server received: '$msg'")
  //    excu.msg2(msg);
  //    Future.value(s"hi $msg")
  //  }

  override def login(user: UserLogin): Future[PassportResult] = {

    println(s" Server receiver :'${user.credential}','${user.password}','${excu.msg2("11")}'")
    val result: PassportResult = PassportResult.apply(user.credential, "登陆成功",
      Option.apply(User.apply("test", "11111", Source.App, Option.apply("aaa"))))
    Future.value(result);
  }

  override def regist(user: User): Future[PassportResult] =  {


    val result: PassportResult = PassportResult.apply(user.mobile, "登陆成功",
      Option.apply(User.apply("test", "11111", Source.App, Option.apply("aaa"))))
    Future.value(result);
  }

  override def userInfo(userId: Long): Future[PassportResult] =  {


    val result: PassportResult = PassportResult.apply("userInfo", "登陆成功",
      Option.apply(User.apply("test", "11111", Source.App, Option.apply("aaa"))))
    Future.value(result);
  }

  override def registerValidate(name: String, value: String): Future[PassportResult] =  {


    val result: PassportResult = PassportResult.apply(name, "登陆成功",
      Option.apply(User.apply("test", "11111", Source.App, Option.apply("aaa"))))
    Future.value(result);
  }
}
