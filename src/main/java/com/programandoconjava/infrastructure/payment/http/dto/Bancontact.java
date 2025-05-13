package com.programandoconjava.infrastructure.payment.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record Bancontact(@JsonProperty("card_last_digits") String cardLastDigits, 
						String name, 
						@JsonProperty("country_code") String countryCode,
						String bic,
						@JsonProperty("iban_last_chars") String ibanLastChars) {

}
