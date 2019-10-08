package com.soprasteria.hackaton.teagile.mail.service.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soprasteria.hackaton.teagile.mail.service.dto.MailDTO;
import com.soprasteria.hackaton.teagile.mail.service.mail.MailClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class QueueConsumer {

	@Autowired
	MailClient mailClient;

	public void receiveMessage(String message) {
		log.info("Received String {} " + message);
		processMessage(message);
	}

	private void processMessage(String message) {
		try {
			MailDTO mailDTO = new ObjectMapper().readValue(message, MailDTO.class);
			mailClient.prepareAndSend(mailDTO);
		} catch (JsonParseException e) {
			log.warn("Bad JSON in message: " + message);
		} catch (JsonMappingException e) {
			log.warn("cannot map JSON to NotificationRequest: " + message);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}