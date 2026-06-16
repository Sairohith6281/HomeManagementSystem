package com.hsms.request.enums;

public enum ServiceRequestStatus {

    CREATED("Created"),
    ASSIGNED("Assigned"),
    ACCEPTED("Accepted"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    private final String displayName;

    ServiceRequestStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
