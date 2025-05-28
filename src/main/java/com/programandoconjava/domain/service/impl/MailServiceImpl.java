package com.programandoconjava.domain.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.programandoconjava.application.exception.AppException;
import com.programandoconjava.domain.model.Client;
import com.programandoconjava.domain.model.Purchase;
import com.programandoconjava.domain.service.ClientsService;
import com.programandoconjava.domain.service.MailService;

@Service
public class MailServiceImpl implements MailService {

	private static final Logger logger = LogManager.getLogger(MailServiceImpl.class);

	@Autowired
	private MailSender mailSender;

	@Autowired
	private ClientsService clientsService;

	@Value("${spring.mail.username}")
	private String from;

	@Override
	public void sendConfirmationEmail(String clientId, String purchaseToken) {
		logger.info("Sending confirmation mail to client={} for the purchase={}", clientId, purchaseToken);

		Optional<Client> client = clientsService.getById(Long.parseLong(clientId));

		if (client.isEmpty()) {
			String errorMsg = String.format("Error sending confirmation mail. Client '%s' not found in database", clientId);
			logger.error(errorMsg);
			throw new AppException(HttpStatus.NOT_FOUND.value(), errorMsg);
		}

		if (client.get().getPurchases() == null || client.get().getPurchases().isEmpty()) {
			String errorMsg = String.format("Error sending confirmation mail. Client '%s' not have purchases registered", clientId);
			logger.error(errorMsg);
			throw new AppException(HttpStatus.NOT_FOUND.value(), errorMsg);
		}

		Optional<Purchase> purchase = client.get().getPurchases().stream().filter(p -> purchaseToken.equals(p.getToken())).findFirst();

		if (purchase.isEmpty()) {
			String errorMsg = String.format("Error sending confirmation mail. Not found Purchase '%s' for Client '%s'", purchaseToken, clientId);
			logger.error(errorMsg);
			throw new AppException(HttpStatus.NOT_FOUND.value(), errorMsg);
		}

		String subject = String.format("Confirmación de compra: %s", purchaseToken);
		String text = new StringBuilder("Gracias por su compra<br><br>")
						.append("El producto debería haberse descargado automaticamente en su navegador. Ante cualquier problema no dude en ponerse en contacto con nosotros.<br><br>")
						.append("<b>Datos de la compra:</b><br>")
						.append("<hr><br>")
						.append("Identificador: <b>").append(purchaseToken).append("</b><br>")
						.append("Producto: <b>").append(purchase.get().getProduct().getName()).append("</b><br>")
						.append("Precio: <b>").append(purchase.get().getTotalAmount()).append(purchase.get().getCurrency()).append("</b> IVA del 21% incluido<br>")
						.append("Fecha de compra: <b>").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("</b><br>")
						.append("<br><br>")
						.append("Si tiene alguna duda o necesita asistencia, no dude en responder a este correo.<br>")
						.append("Gracias por confiar en nosotros.<br><br>")
						.append("Atentamente,<br>")
						.append("El equipo de <b>programandoconjava.com</b>")
						.toString();

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(client.get().getEmail());
		message.setSubject(subject);
		message.setText(text);

		try {
			mailSender.send(message);
		} catch (Exception e) {
			String errorMsg = String.format("Error sending confirmation mail: %s", e.getMessage());
			logger.error(errorMsg);
			throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMsg);
		}
		
	}
}
