package com.itiancai.dao;

import com.itiancai.passport.thrift.PassportResult;
import com.itiancai.passport.thrift.PassportResult$;
import com.itiancai.passport.thrift.Source$;
import com.itiancai.passport.thrift.User$;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import scala.Option;

@Repository
public class ExecuteDao {

  private PassportResult$ myvar = PassportResult$.MODULE$;

  @Autowired
  private Environment environment;

  public PassportResult msg2(String msg){


    System.out.println(222 + "= " + environment.toString());

    return  myvar.apply("1","1", Option.apply(User$.MODULE$.apply("1", "1", Source$.MODULE$.apply(1), Option.apply("11"))));
  }
}
