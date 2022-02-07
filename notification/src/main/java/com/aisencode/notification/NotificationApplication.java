package com.aisencode.notification;

import com.aisencode.amqp.RabbitMQMessageProducer;
import lombok.Getter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(
        scanBasePackages = {
                "com.aisencode.notification",
                "com.aisencode.amqp"
        }
)
public class NotificationApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }

    /*
     * Remove comment to test the publish message
     */

//    @Bean
//    CommandLineRunner commandLineRunner(RabbitMQMessageProducer producer, NotificationConfig notificationConfig) {
//        return args -> {
//           producer.publish(new Person("James", 19), notificationConfig.getInternalExchange(), notificationConfig.getInternalNotificationRoutingKey());
//        };
//    }
//
//    @Getter
//    static class Person {
//        final String name;
//        final int age;
//
//        Person(String name, int age) {
//            this.name = name;
//            this.age = age;
//        }
//    }

}


