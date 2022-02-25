package com.aisencode.customer.dto;

import com.aisencode.customer.Customer;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CustomerRegistrationResponse {

    private String firstName;
    private String lastName;
    private String email;

    @Builder
    public CustomerRegistrationResponse(String firstName, String lastName, String email ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
