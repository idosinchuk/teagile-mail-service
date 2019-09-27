package com.soprasteria.hackaton.teagile.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class MailClient {

	private JavaMailSender mailSender;
	private MailContentBuilder mailContentBuilder;

	@Autowired
	public MailClient(JavaMailSender mailSender, MailContentBuilder mailContentBuilder) {
		this.mailSender = mailSender;
		this.mailContentBuilder = mailContentBuilder;
	}

	public void prepareAndSend(String recipient, String type) {
		MimeMessagePreparator messagePreparator = mimeMessage -> {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
			messageHelper.setFrom("igor.dosinchuk@soprasteria.com");
			messageHelper.setTo(recipient);
			messageHelper.setSubject("Welcome to TEAgile");
			String content = mailContentBuilder.build("Congratulations, your account has been created successfully.");
			messageHelper.setText(content, true);
		};
		try {
			mailSender.send(messagePreparator);
		} catch (MailException e) {
			// runtime exception; compiler will not force you to handle it
		}
	}

}