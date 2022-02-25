package com.aisencode.apigw;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Applying post filter to APIGW
 */

@Slf4j
@Component
public class PostFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            log.info("Global Post-filter executed... from PostFilter.java");
        }));
    }

    /**
     * Assign Order for Post Filter
     * @return Execution Order (Should be reversed)
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
