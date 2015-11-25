package com.itiancai.galaxy.config;


import com.google.common.collect.Maps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * mutile-config
 */
public class ConfigureEnvironment extends StandardEnvironment {

  private static final Logger logger = LoggerFactory.getLogger(ConfigureEnvironment.class);

  private static final String COMMAND_PRAM_ENV = "galaxias.env";

  private static final String DEFAULT_ENV_VAL = "dev";

  private static final String CONFIG_PATH = "/config/${" + COMMAND_PRAM_ENV + "}/**/*.properties";

  @Override
  protected void customizePropertySources(MutablePropertySources propertySources) {

    Map systemEnv = Maps.newHashMap(getSystemEnvironment());
    if (StringUtils.isEmpty(systemEnv.get(COMMAND_PRAM_ENV))) {
      systemEnv.put(COMMAND_PRAM_ENV, DEFAULT_ENV_VAL);
    }
    propertySources.addLast(
        new MapPropertySource(SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, getSystemProperties()));
    propertySources.addLast(
        new SystemEnvironmentPropertySource(SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME,
                                            Collections.unmodifiableMap(systemEnv)));

    String resourceSourcePath = this.resolvePlaceholders(CONFIG_PATH);
    MutablePropertySources
        envPropertySources = this.getPropertySources();
    ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    try {
      Resource[] resources = resolver.getResources(
          ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resourceSourcePath);
      for (Resource resource : resources) {
        envPropertySources.addLast(new ResourcePropertySource(resource));
      }
    } catch (IOException e) {
      logger.error("load config resource failure, path: [" + resourceSourcePath + "]", e);
      throw new RuntimeException(e);
    }
    logger.info("load config resource successful, path : [" + resourceSourcePath + "]");

  }


}
