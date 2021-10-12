package fr.univangers.controller;


import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.univangers.services.SendMailService;


@RestController
public class SendMailController {
	
	String to;
	String Body;
	@Autowired
	JavaMailSender javaMailSender;
	
	@PostMapping("/getMail")
    public String getEmail(@RequestBody String mail ) {
    	
    	this.to = mail.substring(1, mail.length()-1);
        return "Mail received";
    }
	
	@PostMapping("/getBody")
    public String sendMail(@RequestBody String body ) {
    	
    	this.Body = body.substring(1, body.length()-1);

    	
    	SendMailService sendmailservice = new SendMailService(javaMailSender);   
    	try {
    		sendmailservice.sendEmail(to, Body, "récapitulatif du Quiz");
    	}
    	catch(MessagingException e) {
    		
    	}
        return "Mail sent";
    }
    
}
