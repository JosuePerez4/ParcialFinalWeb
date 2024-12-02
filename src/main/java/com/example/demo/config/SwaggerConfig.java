package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.models.GroupedOpenApi;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    GroupedOpenApi publicApi() {
		return GroupedOpenApi.builder().group("public").pathsToMatch("/**").build();
	}

	@Bean
	Info apiInfo() {
		return new Info().title("API de Facturación").description("API para crear y consultar facturas").version("1.0");
	}
}
