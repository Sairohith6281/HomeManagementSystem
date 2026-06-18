package com.hsms.notificationservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hsms.notificationservice.entity.Notification;
import com.hsms.notificationservice.repository.NotificationRepository;
import com.hsms.notificationservice.util.PushNotificationService;
import com.hsms.notificationservice.util.SmsService;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SmsService smsService;

    @Autowired
    private PushNotificationService pushNotificationService;

    @Override
    public Notification saveNotification(Notification notification) {

        Notification savedNotification =
                notificationRepository.save(notification);

        System.out.println("\n================================");
        System.out.println("NOTIFICATION RECEIVED");
        System.out.println("Type    : "
                + savedNotification.getNotificationType());
        System.out.println("User ID : "
                + savedNotification.getUserId());
        System.out.println("Message : "
                + savedNotification.getMessage());
        System.out.println("================================");

        // EMAIL MOCK
        System.out.println("\n================================");
        System.out.println("EMAIL NOTIFICATION SENT");
        System.out.println("Type    : "
                + savedNotification.getNotificationType());
        System.out.println("User ID : "
                + savedNotification.getUserId());
        System.out.println("Message : "
                + savedNotification.getMessage());
        System.out.println("================================");

        // SMS MOCK
        smsService.sendSms(
                "9999999999",
                "[" + savedNotification.getNotificationType() + "] "
                        + savedNotification.getMessage());

        // PUSH MOCK
        pushNotificationService.sendPush(
                savedNotification.getUserId(),
                "[" + savedNotification.getNotificationType() + "] "
                        + savedNotification.getMessage());

        return savedNotification;
    }

    @Override
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    @Override
    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }

    @Override
    public Notification updateNotification(
            Long id,
            Notification notification) {

        Notification existingNotification =
                notificationRepository.findById(id).orElse(null);

        if (existingNotification != null) {

            existingNotification.setUserId(
                    notification.getUserId());

            existingNotification.setNotificationType(
                    notification.getNotificationType());

            existingNotification.setMessage(
                    notification.getMessage());

            existingNotification.setStatus(
                    notification.getStatus());

            return notificationRepository.save(
                    existingNotification);
        }

        return null;
    }

    @Override
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }
}