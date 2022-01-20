package com.aisencode.customer.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CustomerConfig {

    @Bean
    @LoadBalanced // able to run from multiple instances of microservice
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
