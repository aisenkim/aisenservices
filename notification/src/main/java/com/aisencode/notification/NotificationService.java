package com.aisencode.notification;

import com.aisencode.clients.fraud.NotificationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void send(NotificationRequest notificationRequest) {
        Notification notification = Notification.builder()
                .toCustomerId(notificationRequest.getToCustomerId())
                .toCustomerEmail(notificationRequest.getToCustomerName())
                .sender("Aisencode")
                .message(notificationRequest.getMessage())
                .sentAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }

}
