package fr.univangers.services;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class SendMailService {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	public SendMailService (JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
	
	public void sendEmail(String to, String mailBody, String mailTopic) throws MessagingException{
		
		MimeMessage simpleMailMessage = javaMailSender.createMimeMessage();
		boolean multipart = true;
		MimeMessageHelper helper = new MimeMessageHelper(simpleMailMessage,multipart,"utf-8");
		helper.setText(mailBody, true);
		helper.setTo(to);
		helper.setFrom("lordariskoa@gmail.com");
		helper.setSubject(mailTopic);
		this.javaMailSender.send(simpleMailMessage);
		
		
//		simpleMailMessage.setFrom("lordariskoa@gmail.com");
//		simpleMailMessage.setTo(to);
//		simpleMailMessage.setSubject(mailTopic);
//		simpleMailMessage.setText(mailBody);
//		javaMailSender.send(simpleMailMessage);
		
		
		
		
	}
	
	
}