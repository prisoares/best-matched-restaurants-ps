package com.pfs.bestmatchedrestaurantsps.api.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPIConfig() {
    var openAPI = new OpenAPI();
    openAPI.setOpenapi("3.0.0");
    return openAPI;
  }

}
