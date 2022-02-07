package com.aisencode.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(
        scanBasePackages = {  // doint this to use producer from amqp
                "com.aisencode.customer",
                "com.aisencode.amqp",
        }
)
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.aisencode.clients")
public class CustomerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }
}
