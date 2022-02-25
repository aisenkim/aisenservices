package com.aisencode.customer.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerLoginRequest {

    private String email;
    private String password;

    @Builder
    public CustomerLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
