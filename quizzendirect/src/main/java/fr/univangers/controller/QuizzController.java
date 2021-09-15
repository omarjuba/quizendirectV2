package fr.univangers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
public class QuizzController {

    @GetMapping("/quiz")
    public String changeQuestion(Model model) {

        class Info{
            public ArrayList<String> questions;
            public ArrayList<String> reponses;

            Info()
            {
                questions = new ArrayList<String>();
                reponses = new ArrayList<String>();
            }
        };
        Info info = new Info();
        info.questions.add("C++ est-il un langage Orienté Objet ?");
        info.questions.add("A hérite de B es-ce que A a les attributs de B ?");
        info.questions.add("Question numero 3 !");
        info.questions.add("Question numero 4 !");

        info.reponses.add("Oui");
        info.reponses.add("Non");
        info.reponses.add("D'accord");
        info.reponses.add("Possible");
        info.reponses.add("Pas possible");
        info.reponses.add("Impossible");


        model.addAttribute("info", info);
        return "quiz";
    }

}