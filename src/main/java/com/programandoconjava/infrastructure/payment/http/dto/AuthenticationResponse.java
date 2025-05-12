package com.programandoconjava.infrastructure.payment.http.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public record AuthenticationResponse(String scope, 
									@JsonProperty("access_token") String accessToken, 
									@JsonProperty("token_type") String tokenType,
									@JsonProperty("app_id") String appId,
									@JsonProperty("expires_in") long expiresIn,
									String nonce) {}
