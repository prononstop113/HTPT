package com.HTPT.Gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringCloudConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("download",r -> r.path("/download**")
                        .filters(f -> f.rewritePath("/download(?<segment>.*)", "/client/download${segment}"))
                        .uri("lb://file-server")
                )
                .route("upload",r -> r.path("/upload")
                        .filters(f -> f.rewritePath("/upload", "/client/upload"))
                        .uri("lb://file-server")
                )
                .route("get file list",r -> r.path("/get-list-file")
                        .filters(f -> f.rewritePath("/get-list-file", "/client/get-list-file/"))
                        .uri("lb://file-server"))
                .route("get file list",r -> r.path("/test")
                        .uri("lb://file-server"))
                .build();
    }

}