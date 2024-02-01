package com.aalonso.CarRegistry.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class SwaggerConfig {

	@PostConstruct
	public void init() {
		log.info("SwaggerConfig is operational...");
	}

	@Bean
	public OpenAPI api() {
		return new OpenAPI().openapi("3.1.0")
				.info(info());
	}

	protected Info info() {
		return new Info().title("Vehicle registry API")
				.description("Api to manage the vehicle registry")
				.version("1.0.0");
	}
}
