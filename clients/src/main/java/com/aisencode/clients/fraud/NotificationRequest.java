package com.aisencode.clients.fraud;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationRequest {

    private Integer toCustomerId;
    private String toCustomerName;
    private String message;

    @Builder
    public NotificationRequest(Integer toCustomerId, String toCustomerName, String message) {
        this.toCustomerId = toCustomerId;
        this.toCustomerName = toCustomerName;
        this.message = message;
    }

}
