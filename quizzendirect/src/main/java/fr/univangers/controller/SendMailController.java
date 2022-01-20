package fr.univangers.controller;


import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.univangers.services.SendMailService;


@RestController
public class SendMailController {
	@Autowired
	JavaMailSender javaMailSender;
	
	@PostMapping("/api/sendMail")
    public ResponseEntity<?> sendMail(
    		@RequestBody SendMailParams params
    		) {
    	String mail_receiver = params.receiver;
    	String mail_content = params.content;

		String mail_receiver_formatted = mail_receiver.substring(1, mail_receiver.length()-1);
    	String mail_content_formatted = mail_content.substring(1, mail_content.length()-1);

    	SendMailService sendmailservice = new SendMailService(javaMailSender);   
    	try {
    		sendmailservice.sendEmail(mail_receiver_formatted, mail_content_formatted, "Récapitulatif du Quiz");
    		return ResponseEntity.ok("Le mail a été envoyé avec succès");  

    	}
    	catch(MessagingException e) {    		
    		return ResponseEntity.ok("Le mail n'a pas pu être envoyé");  
    	}
    }
    
}
