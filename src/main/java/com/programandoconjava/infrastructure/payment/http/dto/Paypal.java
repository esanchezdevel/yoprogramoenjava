package com.programandoconjava.infrastructure.payment.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record Paypal(@JsonProperty("account_status") String accountStatus, 
					@JsonProperty("phone_type") String phoneType,
					@JsonProperty("business_name") String businessName, 
					@JsonProperty("email_address") String emailAddress,
					@JsonProperty("account_id") String accountId,
					Name name,
					@JsonProperty("phone_number") PhoneNumber phoneNumber,
					@JsonProperty("birth_date") String birthDate,
					Address address) {

}
