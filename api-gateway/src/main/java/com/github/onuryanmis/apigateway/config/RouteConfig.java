package com.github.onuryanmis.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("category-service", r -> r.path("/api/v1/category/**")
                        .uri("lb://category-service"))
                .route("task-service", r -> r.path("/api/v1/task/**")
                        .uri("lb://task-service"))
                .build();
    }
}
