package com.programandoconjava.infrastructure.payment.http.dto;

public record PurchaseUnit(Item[] items, Amount amount) {

}
