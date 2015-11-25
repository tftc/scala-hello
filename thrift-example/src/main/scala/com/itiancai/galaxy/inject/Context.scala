package com.itiancai.galaxy.inject

import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.core.env.{ConfigurableEnvironment}

import scala.reflect._


private[inject] object ContextHolder {

  private[this] val appContext: AnnotationConfigApplicationContext = new AnnotationConfigApplicationContext()

  private[inject] def env(environment: ConfigurableEnvironment) = {
    appContext.setEnvironment(environment)
    this
  }

  private[inject] def config(configure: ContextConfig) = {
    appContext.scan(configure.scanPackageName() ++ Seq("com.itiancai.galaxy"): _*)
    appContext.register(configure.registerClass():_*)
    this
  }

  private[inject] def refresh = {
    appContext.refresh()
  }

  private[inject] def binder: Binder = {
    Binder(appContext.getBeanFactory)
  }

  private[inject] def injector: Injector = {
    Injector(appContext.getBeanFactory)
  }

}

case class Binder(beanFactory: ConfigurableBeanFactory) {
  def bind(anyRef: AnyRef): Unit = {
    beanFactory.registerSingleton(anyRef.getClass.getCanonicalName,anyRef)
  }
}

case class Injector(underlying: BeanFactory) {

  def instance[T: ClassTag]: T = underlying.getBean(classTag[T].runtimeClass.asInstanceOf[Class[T]]);

  //  def instance[T: Manifest, Ann <: JavaAnnotation : Manifest]: T = {
  //    val annotationType = manifest[Ann].runtimeClass.asInstanceOf[Class[Ann]]
  //    val key = Key.get(typeLiteral[T], annotationType)
  //    underlying.getInstance(key)
  //  }

  def instance[T: Manifest](name: String): T = {
    underlying.getBean(name).asInstanceOf[T]
  }

  def instance[T](clazz: Class[T]): T = underlying.getBean(clazz)

  // def instance[T](key: Key[T]): T = underlying.getInstance(key)
}


trait ContextConfig{

  def scanPackageName(): Seq[String];

  def registerClass(): Seq[Class[_]];
}



