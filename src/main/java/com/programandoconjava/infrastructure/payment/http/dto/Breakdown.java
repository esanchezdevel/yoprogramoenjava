package com.programandoconjava.infrastructure.payment.http.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Breakdown(@JsonProperty("item_total") UnitAmount itemTotal) {

}
