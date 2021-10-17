package fr.univangers.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendMailService {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	public void sendEmail(String to, String mailBody, String mailTopic) {
		
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom("lordariskoa@gmail.com");
		simpleMailMessage.setTo(to);
		simpleMailMessage.setSubject(mailTopic);
		simpleMailMessage.setText(mailBody);
		javaMailSender.send(simpleMailMessage);
		
		
		
		
	}
	
	
}