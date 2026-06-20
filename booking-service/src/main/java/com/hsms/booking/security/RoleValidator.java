package com.hsms.booking.security;

import java.util.Arrays;

public class RoleValidator {

	public static void validate(String role, String... allowedRoles) {

		boolean allowed = Arrays.stream(allowedRoles).anyMatch(r -> r.equals(role));

		if (!allowed) {

			throw new RuntimeException("Access Denied");
		}
	}
}