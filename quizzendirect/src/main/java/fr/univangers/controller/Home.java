package fr.univangers.controller;

import fr.univangers.models.Enseignant;
import fr.univangers.models.Repertoire;
import fr.univangers.services.QuestionService;
import fr.univangers.services.RepertoireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Home {

    @Autowired
    RepertoireService repertoireService;
    @Autowired
    QuestionService questionServiceService;

    @GetMapping("/")
    public String home() {
        return "home";
    }
    @GetMapping("/i")
    public String i() {
        return "index";
    }

    @GetMapping("/i2")
    public String i2() {
        return "index2";
    }


}
