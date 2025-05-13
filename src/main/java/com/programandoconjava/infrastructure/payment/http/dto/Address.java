package com.programandoconjava.infrastructure.payment.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record Address(@JsonProperty("address_line_1") String addressLine1,
					@JsonProperty("address_line_2") String addressLine2,
					@JsonProperty("admin_area_1") String adminArea1,
					@JsonProperty("admin_area_2") String adminArea2,
					@JsonProperty("postal_code") String postalCode,
					@JsonProperty("country_code") String countryCode) {

}
