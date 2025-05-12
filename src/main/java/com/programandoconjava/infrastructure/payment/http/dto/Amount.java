package com.programandoconjava.infrastructure.payment.http.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Amount(@JsonProperty("currency_code") String currencyCode, String value, Breakdown breakdown) {

}
