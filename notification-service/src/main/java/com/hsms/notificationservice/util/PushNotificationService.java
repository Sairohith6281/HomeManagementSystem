package com.hsms.notificationservice.util;

import org.springframework.stereotype.Service;

@Service
public class PushNotificationService {

    public void sendPush(
            Long userId,
            String message) {

        System.out.println(
                "================================");

        System.out.println(
                "PUSH NOTIFICATION SENT");

        System.out.println(
                "User : "
                        + userId);

        System.out.println(
                "Message : "
                        + message);

        System.out.println(
                "================================");
    }
}