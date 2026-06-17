package com.hsms.exception;

public class TechnicianNotFoundException extends RuntimeException {
    public TechnicianNotFoundException(String message) {
        super(message);
    }
}