package com.programandoconjava.infrastructure.payment.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record Payer(@JsonProperty("email_address") String emailAddress,
					@JsonProperty("payer_id") String payerId,
					Name name, 
					Phone phone,
					@JsonProperty("birth_date") String birthDate,
					Address address) {

}
