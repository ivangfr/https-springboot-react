package com.ivanfranchin.moviesapi.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

  @Bean
  OpenAPI customOpenAPI(@Value("${spring.application.name}") String applicationName) {
    return new OpenAPI().components(new Components()).info(new Info().title(applicationName));
  }

  @Bean
  GroupedOpenApi customApi() {
    return GroupedOpenApi.builder().group("api").pathsToMatch("/api/**").build();
  }
}
