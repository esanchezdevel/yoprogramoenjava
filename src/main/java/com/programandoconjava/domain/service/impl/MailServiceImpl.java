package com.programandoconjava.domain.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.programandoconjava.application.exception.AppException;
import com.programandoconjava.domain.model.Client;
import com.programandoconjava.domain.model.Purchase;
import com.programandoconjava.domain.service.ClientsService;
import com.programandoconjava.domain.service.MailService;

import jakarta.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements MailService {

	private static final Logger logger = LogManager.getLogger(MailServiceImpl.class);

	@Autowired
	private JavaMailSender mailSender;

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
						.append("Comprador: <b>").append(client.get().getName()).append(" ").append(client.get().getSurname()).append("</b><br>")
						.append("Producto: <b>").append(purchase.get().getProduct().getName()).append("</b><br>")
						.append("Precio: <b>").append(purchase.get().getTotalAmount()).append(purchase.get().getCurrency()).append("</b> IVA del 21% incluido<br>")
						.append("Fecha de compra: <b>").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("</b><br>")
						.append("<br><br>")
						.append("Si tiene alguna duda o necesita asistencia, no dude en responder a este correo.<br>")
						.append("Gracias por confiar en nosotros.<br><br>")
						.append("Atentamente,<br>")
						.append("El equipo de <b>programandoconjava.com</b>")
						.toString();

		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

			helper.setFrom(from);
			helper.setTo(client.get().getEmail());
			helper.setSubject(subject);
			helper.setText(text, true);

			mailSender.send(mimeMessage);
		} catch (Exception e) {
			String errorMsg = String.format("Error sending confirmation mail: %s", e.getMessage());
			logger.error(errorMsg);
			throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMsg);
		}
	}
}
