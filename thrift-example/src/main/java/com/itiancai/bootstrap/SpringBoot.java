package com.itiancai.bootstrap;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;

import javax.annotation.Resource;
import javax.inject.Inject;

@Configuration
@ComponentScan(basePackages = "com.itiancai")
@PropertySource("classpath:/config/${galaxias.env}/jdbc.properties")
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

  @Bean
  public Environment getEnvironment() {
    Environment environment = context.getEnvironment();
    //environment.`
    return environment;
  }

}
