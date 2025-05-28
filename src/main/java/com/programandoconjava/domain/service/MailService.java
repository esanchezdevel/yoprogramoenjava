package com.programandoconjava.domain.service;

import com.programandoconjava.application.exception.AppException;

public interface MailService {

	/**
	 * Send purchase confirmation mail
	 * @param clientId The id of the client stored in our database
	 * @param purchaseToken The token that identifies the unique purchase
	 * @throws AppException
	 */
	void sendConfirmationEmail(String clientId, String purchaseToken) throws AppException;
}
