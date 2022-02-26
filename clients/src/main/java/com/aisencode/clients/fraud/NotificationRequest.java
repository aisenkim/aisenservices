package com.aisencode.clients.fraud;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationRequest {

    private Long toCustomerId;
    private String toCustomerName;
    private String message;

    @Builder
    public NotificationRequest(Long toCustomerId, String toCustomerName, String message) {
        this.toCustomerId = toCustomerId;
        this.toCustomerName = toCustomerName;
        this.message = message;
    }

}
