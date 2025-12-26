package com.gym.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
  @Autowired
  private InterceptorConfig interceptorConfig;
  
  @Autowired
  private RoleInterceptor roleInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(interceptorConfig)
        .addPathPatterns("/**")
        .excludePathPatterns("/auth/login", "/auth/register");
    
    registry.addInterceptor(roleInterceptor)
        .addPathPatterns("/**")
        .excludePathPatterns("/auth/login", "/auth/register");
  }
}
