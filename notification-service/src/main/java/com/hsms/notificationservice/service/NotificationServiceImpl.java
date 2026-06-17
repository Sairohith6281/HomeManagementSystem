package com.hsms.notificationservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hsms.notificationservice.entity.Notification;
import com.hsms.notificationservice.repository.NotificationRepository;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
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
    public Notification updateNotification(Long id, Notification notification) {

        Notification existingNotification =
                notificationRepository.findById(id).orElse(null);

        if (existingNotification != null) {

            existingNotification.setUserId(notification.getUserId());
            existingNotification.setMessage(notification.getMessage());
            existingNotification.setStatus(notification.getStatus());

            return notificationRepository.save(existingNotification);
        }

        return null;
    }

    @Override
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }
}