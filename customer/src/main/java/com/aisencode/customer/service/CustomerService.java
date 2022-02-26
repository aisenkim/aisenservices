package com.aisencode.customer.service;

import com.aisencode.amqp.RabbitMQMessageProducer;
import com.aisencode.clients.fraud.FraudCheckResponse;
import com.aisencode.clients.fraud.FraudClient;
import com.aisencode.clients.fraud.NotificationRequest;
import com.aisencode.customer.Customer;
import com.aisencode.customer.Role;
import com.aisencode.customer.repository.RoleRepository;
import com.aisencode.customer.security.UserPrincipal;
import com.aisencode.customer.dto.CustomerRegistrationRequest;
import com.aisencode.customer.dto.CustomerRegistrationResponse;
import com.aisencode.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomerService implements UserDetailsService {

    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;
    private final FraudClient fraudClient;

    private final RabbitMQMessageProducer rabbitMQMessageProducer;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public CustomerRegistrationResponse registerCustomer(CustomerRegistrationRequest request) {

        // ENCRYPT PASSWORD (SPRING SECURITY)
        String encryptedPassword = this.bCryptPasswordEncoder.encode(request.getPassword());

        Collection<Role> roles = new ArrayList<>();
        for (String roleName : request.getRoles()) {
            Role roleEntity = roleRepository.findByName(roleName);
            if(roleEntity != null)
                roles.add(roleEntity);
        }

        Customer customer = Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(encryptedPassword)
                .roles(roles)
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

        return new CustomerRegistrationResponse(request.getFirstName(), request.getLastName(), request.getEmail());
    }

    public Customer getCustomerDetailsByEmail(String email) {
        Customer foundCustomer = customerRepository.findCustomerByEmail(email);
        if (foundCustomer == null) {
            throw new UsernameNotFoundException(email);
        }

        return foundCustomer;
    }

    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer foundCustomer = customerRepository.findCustomerByEmail(username);
        if (foundCustomer == null) {
            throw new UsernameNotFoundException(username);
        }
        // UserPrincipal - HELPER CLASS for the bottom return statement
        return new UserPrincipal(foundCustomer);

        // Will comment bottom line to create our own class to make things less complicated - [ADDING ROLES/AUTHORITIES]
        // SET third parameter to false if you want to enable it after email verification completes
        //return new User(foundCustomer.getEmail(), foundCustomer.getPassword(), true, true, true, true, new ArrayList<>());

    }
}
