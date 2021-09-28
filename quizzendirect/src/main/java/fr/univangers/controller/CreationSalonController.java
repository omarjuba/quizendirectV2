package fr.univangers.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import fr.univangers.User;

@Controller
public class CreationSalonController {
    @GetMapping("/creationSalon")
    public String getSelectedPage(Model model, @CookieValue(value="userEmail",defaultValue = "") String userEmail){
    	
    	if(!userEmail.equals("")) // authentifier
            return "creationSalon";//retourne creationSalon
        else {//non authentifier
            model.addAttribute("user", new User());
            return "comptePage"; //vers lpage d'authentification
        }
    }	
    	
}