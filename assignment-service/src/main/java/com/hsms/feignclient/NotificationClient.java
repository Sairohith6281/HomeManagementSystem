package com.hsms.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.hsms.model.NotificationDTO;

@FeignClient(name = "notification-service")
public interface NotificationClient {
	@PostMapping("/api/notifications/send")
    void sendNotification(@RequestBody NotificationDTO notification);
}