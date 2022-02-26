package com.aisencode.customer.dto;

import com.aisencode.customer.Customer;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@Getter
@NoArgsConstructor
public class CustomerRegistrationRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Collection<String> roles;

    @Builder
    public CustomerRegistrationRequest(String firstName, String lastName, String email, String password, Collection<String> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    // Convert roles string to role type for later use
    public Customer toEntity() {
        return Customer.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .build();
    }
}
