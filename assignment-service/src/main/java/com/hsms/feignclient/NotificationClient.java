package com.hsms.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "notification-service")
public interface NotificationClient {

	@PostMapping("/api/notifications/send")
	void sendNotification(@RequestParam String recipient, @RequestParam String message);
}