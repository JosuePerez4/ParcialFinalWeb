package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.models.GroupedOpenApi;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

	@Bean
	public GroupedOpenApi facturaApi() {
		return GroupedOpenApi.builder().group("factura").pathsToMatch("/crear/**", "/consultar/**").build();
	}

	@Bean
	public Info apiInfo() {
		return new Info().title("API de Facturación").description("API para crear y consultar facturas").version("1.0");
	}
}
