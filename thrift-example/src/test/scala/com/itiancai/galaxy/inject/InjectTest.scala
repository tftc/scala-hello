package com.itiancai.galaxy.inject

import org.junit.runner.RunWith
import org.scalatest.WordSpec
import org.scalatest.junit.JUnitRunner
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.stereotype.Component

@RunWith(classOf[JUnitRunner])
class InjectTest extends WordSpec{

     "A class" in {

       val applicationContext = new AnnotationConfigApplicationContext();
       applicationContext.register((new TestA()).getClass);
       val test = Injector(applicationContext)

       println(test.instance[TestA].getClass)
       println(test.instance((new TestA()).getClass).getClass)
       println(test.instance("tt"))
     }

}

@Component("tt")
class TestA{

}
