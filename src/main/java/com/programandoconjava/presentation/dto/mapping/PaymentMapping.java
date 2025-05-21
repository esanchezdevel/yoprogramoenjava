package com.programandoconjava.presentation.dto.mapping;

import com.programandoconjava.infrastructure.payment.http.dto.CaptureOrderResponse;
import com.programandoconjava.infrastructure.payment.http.dto.CreateOrderResponse;
import com.programandoconjava.presentation.dto.CaptureOrderResponseDTO;
import com.programandoconjava.presentation.dto.CreateOrderResponseDTO;

public class PaymentMapping {

	public static CreateOrderResponseDTO parseCreateOrderResponseToDTO(CreateOrderResponse createOrderResponse) {
		return new CreateOrderResponseDTO(createOrderResponse.getId());
	}

	public static CaptureOrderResponseDTO parseCaptureOrderResponseToDTO(CaptureOrderResponse captureOrderResponse) {
		return new CaptureOrderResponseDTO(captureOrderResponse.getStatus());
	}
}
