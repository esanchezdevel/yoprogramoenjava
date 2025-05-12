package com.programandoconjava.infrastructure.payment.http.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UnitAmount(@JsonProperty("currency_code") String currencyCode, String value) {

}
