package com.example.apigateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements GlobalFilter {
    private Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    // Every time a request is made to API Gateway Server, it will be logged in this function with all the information about the request present!
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Logging information about the request (that information is present in the 1st parameter = exchange)
        this.logger.info("Path of the request received -> {}", exchange.getRequest().getURI());
        // After logging the request, we want to let the execution continue:
        return chain.filter(exchange);
        // So, we're logging and letting the chain continue
    }
    // If you want to implement authentication for all the requests, then this might be a right place to implement that.
}
