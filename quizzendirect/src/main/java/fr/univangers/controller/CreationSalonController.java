package fr.univangers.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CreationSalonController {
    @GetMapping("/creationSalon")
    public String getSelectedPage(){
        return "creationSalon";
    }
}