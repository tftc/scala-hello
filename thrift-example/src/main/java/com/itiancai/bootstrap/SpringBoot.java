package com.itiancai.bootstrap;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.itiancai")
public class SpringBoot {

  private static AnnotationConfigApplicationContext context;



  // public static
  protected static void init(){
    context = new AnnotationConfigApplicationContext();
    context.register(SpringBoot.class);
    context.refresh();
  }

  public static final AnnotationConfigApplicationContext context(){
    if(context == null){
      init();
    }
    return context;
  }

}
