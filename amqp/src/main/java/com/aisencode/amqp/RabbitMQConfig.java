package com.aisencode.amqp;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@AllArgsConstructor
@Configuration
public class RabbitMQConfig {

    /**
     * Process : Message -> JSON -> Java Object
     */

    private final ConnectionFactory connectionFactory;

    /*
     * Rabbit Template allows us to send messages to the queue (AS JSON)
     * Making function to create custom template
     */
    @Bean
    public AmqpTemplate amqpTemplate() {
        log.info("Using my amqpTemplate *****************");
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jacksonConverter());
        return rabbitTemplate;
    }

    /*
     * Allows us to receive message from the queue using the jackson converter
     * Process: JSON -> JAVA OBJECT
     */
    @Bean
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jacksonConverter());
        return factory;
    }

    @Bean
    public MessageConverter jacksonConverter() {
        MessageConverter jackson2JsonMessaageConverter = new Jackson2JsonMessageConverter();
        return jackson2JsonMessaageConverter;
    }
}
