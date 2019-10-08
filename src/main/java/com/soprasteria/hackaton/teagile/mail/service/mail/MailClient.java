package com.soprasteria.hackaton.teagile.mail.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.soprasteria.hackaton.teagile.mail.service.dto.MailDTO;

@Service
public class MailClient {

	private JavaMailSender javaMailSender;
	private MailContentBuilder mailContentBuilder;

	@Autowired
	public MailClient(JavaMailSender mailSender, MailContentBuilder mailContentBuilder) {
		this.javaMailSender = mailSender;
		this.mailContentBuilder = mailContentBuilder;
	}

	public void prepareAndSend(MailDTO mailDTO, String type) {
		MimeMessagePreparator messagePreparator = mimeMessage -> {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
			messageHelper.setFrom("teagilehackaton@gmail.com");
			messageHelper.setTo(mailDTO.getEmail());
			messageHelper.setSubject("Welcome to TEAgile");
			String content = mailContentBuilder.build("Congratulations, your account has been created successfully.");
			messageHelper.setText(content, true);
		};
		
		try {
			javaMailSender.send(messagePreparator);
		} catch (MailException e) {
			// runtime exception; compiler will not force you to handle it
		}
	}

}