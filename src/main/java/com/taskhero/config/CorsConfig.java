package com.taskhero.config;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@AllArgsConstructor
public class CorsConfig implements WebMvcConfigurer {
  private final Dotenv dotenv;

  @Override
  public void addCorsMappings(@NonNull CorsRegistry registry) {
    registry
        .addMapping("/**")
        .allowedOrigins(dotenv.get("FRONT_END_URL"))
        .maxAge(3600)
        .allowedMethods("GET", "PUT", "POST", "DELETE")
        .allowedHeaders("*");
  }
}
