package com.riwi.m2w1.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Swagger / OpenAPI.
 *
 * Solo documenta las rutas /api/** (REST).
 * Las rutas /admin/** (vistas Thymeleaf) quedan excluidas.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI eventifyOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Eventify API")
                        .description("API REST para gestión de eventos y lugares. " +
                                "Panel visual en /admin/events y /admin/venues.")
                        .version("2.0.0"));
    }

    /**
     * pathsToMatch("/api/**") → Swagger solo escanea rutas REST.
     */
    @Bean
    public GroupedOpenApi apiGroup() {
        return GroupedOpenApi.builder()
                .group("api")
                .pathsToMatch("/api/**")
                .build();
    }
}
