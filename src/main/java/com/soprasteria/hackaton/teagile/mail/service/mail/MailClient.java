package com.soprasteria.hackaton.teagile.mail.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.soprasteria.hackaton.teagile.mail.service.dto.MailDTO;

@Service
public class MailClient {
	
	@Value("${spring.mail.username}")
	private String mailFrom;
	
	@Value("{registration.mail.subject}")
	private String registrationSubject;
	
	@Value("{registration.mail.text}")
	private String registrationText;
	
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
			// runtime exception; compiler will not force you to handle it
		}
	}

}