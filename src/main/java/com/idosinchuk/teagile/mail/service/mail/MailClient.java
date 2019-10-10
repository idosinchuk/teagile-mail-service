package com.idosinchuk.teagile.mail.service.mail;

import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.idosinchuk.teagile.mail.service.dto.MailDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MailClient {

	@Value("${spring.mail.username}")
	private String mailFrom;

	// Get properties from messages.properties
	ResourceBundle bundle = ResourceBundle.getBundle("messages");
	private String registrationSubject = bundle.getString("registration.mail.subject");
	private String registrationText = bundle.getString("registration.mail.text");

	private JavaMailSender javaMailSender;
	private MailContentBuilder mailContentBuilder;

	@Autowired
	public MailClient(JavaMailSender mailSender, MailContentBuilder mailContentBuilder) {
		this.javaMailSender = mailSender;
		this.mailContentBuilder = mailContentBuilder;
	}

	public void prepareAndSend(MailDTO mailDTO) {
		MimeMessagePreparator messagePreparator = mimeMessage -> {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
			messageHelper.setFrom(mailFrom);
			messageHelper.setTo(mailDTO.getEmail());
			messageHelper.setSubject(registrationSubject);
			String content = mailContentBuilder.build(registrationText);
			messageHelper.setText(content, true);
		};

		try {
			javaMailSender.send(messagePreparator);
		} catch (MailException e) {
			log.error("There was an error {} " + e.getMessage());
		}
	}

}