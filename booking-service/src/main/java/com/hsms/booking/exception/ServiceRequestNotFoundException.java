package com.hsms.booking.exception;

public class ServiceRequestNotFoundException extends RuntimeException {
	public ServiceRequestNotFoundException(String message) {
		super(message);
	}
}
