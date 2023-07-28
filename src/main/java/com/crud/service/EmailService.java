package com.crud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService 
{
	@Autowired
	private JavaMailSender javaMailSender;
	
	public boolean sendMail(String message,String subject,String to)
	{
		boolean f=false;
		try {
			
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			
			MimeMessageHelper helper=new MimeMessageHelper(mimeMessage);
			
			helper.setFrom("chakravartisumit11@gmail.com");
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(message);
			
			javaMailSender.send(mimeMessage);
			f=true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}
}
