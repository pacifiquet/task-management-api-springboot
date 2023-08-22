package com.taskhero.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(@NonNull CorsRegistry registry) {
    registry
        .addMapping("/**")
        .allowedOrigins("http://localhost:4200/")
        .maxAge(3600)
        .allowedMethods("GET", "PUT", "POST", "DELETE")
        .allowedHeaders("*");
  }
}
