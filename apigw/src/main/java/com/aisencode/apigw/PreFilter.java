package com.aisencode.apigw;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * All incoming HTTP requests will hit this point
 * Can add pre filters before request reaches the destination
 */

@Slf4j
@Component
public class PreFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("My first pre-filter is executed");

        String requestPath = exchange.getRequest().getPath().toString();// returns server http request
        log.info("Request Path = " + requestPath);

        HttpHeaders headers = exchange.getRequest().getHeaders();
        Set<String> headerNames = headers.keySet(); // Extract key value of headers
        headerNames.forEach((headerName) -> {
            String headerValue = headers.getFirst(headerName);
            log.info(headerName + ": " + headerValue);
        });

        return chain.filter(exchange);
    }

    /**
     * Assign Execution Order for Filters
     *
     * @return Order Index
     */
    @Override
    public int getOrder() {
        return 0; // Will be executed first
    }
}
