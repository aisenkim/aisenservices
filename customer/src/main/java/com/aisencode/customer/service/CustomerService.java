package com.aisencode.customer.service;

import com.aisencode.amqp.RabbitMQMessageProducer;
import com.aisencode.clients.fraud.FraudCheckResponse;
import com.aisencode.clients.fraud.FraudClient;
import com.aisencode.clients.fraud.NotificationRequest;
import com.aisencode.customer.Customer;
import com.aisencode.customer.dto.CustomerRegistrationRequest;
import com.aisencode.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final FraudClient fraudClient;

    private final RabbitMQMessageProducer rabbitMQMessageProducer;

    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .build();

        // TODO  - check if email valid
        // TODO - check if email not taken
        customerRepository.saveAndFlush(customer);

        // TODO  - check if fraudster
//        FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(
//                "http://FRAUD/api/v1/fraud-check/{customerId}",
//                FraudCheckResponse.class,
//                customer.getId()
//        );

        // CHANGED FROM ABOVE CODE AFTER USING OPEN FEIGN
        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());

        if (fraudCheckResponse == null) {
            throw new NullPointerException("Fraud check has failed");
        }

        if (fraudCheckResponse.getIsFraudster()) {
            throw new IllegalStateException("fraudster");
        }

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .toCustomerId(customer.getId())
                .toCustomerName(customer.getEmail())
                .message(String.format("Hi %s welcome to Aisencode...", customer.getFirstName()))
                .build();


        // async add message to queue
        rabbitMQMessageProducer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );

    }

}
