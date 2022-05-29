package com.example.apigateway.configuration;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class ApiGatewayConfiguration {
    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) { // using this builder we can customise the routes which we want to use
        Function<PredicateSpec, Buildable<Route>> routeFunctionGet = p -> p.path("/get")
                .filters(f -> f
                        .addRequestHeader("MyHeader", "MyURI") // ex: these can be your authentication headers
                        .addRequestParameter("Param", "MyValue")) // ex: these can be your authentication parameters
                .uri("http://httpbin.org:80"); // if request comes to /get then redirect => ex: you can redirect to your microservice

        return builder.routes()
                .route(routeFunctionGet).build();
    }
}
