package com.itiancai.passport.thrift

//import com.twitter.inject.Logging
import com.itiancai.passport.dao.ExecuteDao
import com.twitter.util.Future
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class PassportServiceImpl extends PassportService.FutureIface {

  val log = LoggerFactory.getLogger(getClass)

  @Autowired
  var excu: ExecuteDao = null;


  override def login(user: UserLogin): Future[PassportResult] = {
    log.info(s" Server receiver :'${user.credential}','${getClass}','${excu.msg2("11")}'")
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
