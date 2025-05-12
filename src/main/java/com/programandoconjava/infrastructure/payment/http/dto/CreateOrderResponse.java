package com.programandoconjava.infrastructure.payment.http.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public record CreateOrderResponse(String id, String status, List<Link> links) {}
