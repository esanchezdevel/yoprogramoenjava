package com.programandoconjava.infrastructure.payment.http.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Item(String name, String quantity, @JsonProperty("unit_amount") UnitAmount unitAmount) {

}
