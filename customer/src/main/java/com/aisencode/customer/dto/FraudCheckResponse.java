package com.aisencode.customer.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FraudCheckResponse {

    private Boolean isFraudster;

    @Builder
    public FraudCheckResponse(Boolean isFraudster) {
        this.isFraudster = isFraudster;
    }

}
