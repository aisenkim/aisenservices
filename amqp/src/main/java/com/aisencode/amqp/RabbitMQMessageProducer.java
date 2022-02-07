package com.aisencode.amqp;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Slf4j
@Component
public class RabbitMQMessageProducer {

    private final AmqpTemplate amqpTemplate;

    /**
     * Publishing message to exchange
     * @param payload - Object type to handle different types of payload
     * @param exchange
     * @param routingKey
     */
    public void publish(Object payload, String exchange, String routingKey) {
        log.info("Publishing to {} using routing key {}. Payload: {}", exchange, routingKey, payload);
        amqpTemplate.convertAndSend(exchange, routingKey, payload);
        log.info("Published to {} using routing key {}. Payload: {}", exchange, routingKey, payload);
    }

}
