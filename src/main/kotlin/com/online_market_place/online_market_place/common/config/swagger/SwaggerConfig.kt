package com.online_market_place.online_market_place.common.config.swagger

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Online Marketplace API")
                    .description("API Documentation for Online Marketplace")
                    .version("2.0")
            )
            // Add security requirement for all endpoints
            .addSecurityItem(
                SecurityRequirement()
                    .addList("Bearer")
            )
            // Define the security scheme
            .components(
                Components()
                    .addSecuritySchemes(
                        "Bearer",
                        SecurityScheme()
                            .name("Authorization")
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("Bearer")
                            .bearerFormat("JWT")
                            .`in`(SecurityScheme.In.HEADER)
                    )
            )
    }
}