package com.hsms.request.enums;

public enum AssignmentStatus {

	PENDING("Pending"), ACCEPTED("Accepted"), REJECTED("Rejected");

	private final String displayName;

	AssignmentStatus(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
