package com.hsms.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("token_type")
	private String tokenType = "Bearer";

	@JsonProperty("expires_in")
	private Long expiresIn;

	private Long userId;

	private String email;

	private String name;

	private String role;

	private Boolean success;

	private String message;
}
