package com.aisencode.apigw;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Mono;

/**
 * Contains both PRE and POST filters,
 * instead of having a seperate files
 */

@Slf4j
@Configuration
public class GlobalFiltersConfig {

    // SECOND POST FILTER
    @Order(1)
    @Bean
    public GlobalFilter secondPrePostFilter() {

        // ORDER -> 1 (Pre filter)
        return ((exchange, chain) -> {
            log.info("My second global pre-filter executed");

            // ORDER -> 1 (Post filter)
            // ADDING A .then will also trigger a post filter
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info("My second global post filter was executed...");
            }));
        });

    }

    // ABLE TO ADD MORE FILTERS by duplicating the code above

    // THIRD PRE / POST FILTER
    @Order(2)
    @Bean
    public GlobalFilter thirdPrePostFilter() {

        // ORDER -> 2 (PRE FILTER)
        return ((exchange, chain) -> {
            log.info("My third global pre-filter executed");

            // ORDER -> 3 (POST FILTER)
            // ADDING A .then will also trigger a post filter
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info("My third global post filter was executed...");
            }));
        });

    }
}
