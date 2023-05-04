package br.com.boleto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;

@Configuration
@SecurityScheme(name = "apiKey", type = SecuritySchemeType.HTTP, scheme = "bearer")
@SecurityRequirement(name = "Bearer Authentication")
@OpenAPIDefinition(info = @Info(title = "Boleto Bancos API", version = "v1"), security = @SecurityRequirement(name = "apiKey")) // references the name defined in the line 3 
public class SwaggerConfig {

	@Bean
    public OpenAPI swaggerPlugin() {
        return new OpenAPI().components(new Components());
    }
	
}