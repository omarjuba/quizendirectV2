

package fr.univangers.controller;

import fr.univangers.User;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GestionQuestionsController {

    @GetMapping("/GestionQuestions")
    public String getGestionQuestions(Model model,@CookieValue(value="userEmail",defaultValue = "") String userEmail) {
        // Test si l'enseignant est connecté
        //if(!userEmail.equals(""))
            return "GestionQuestions";
        // Si l'enseignant n'est pas connecté, on le redirige vers la page de connexion
       /* else {
            model.addAttribute("user", new User());
            return "comptePage";
        }*/
    }

}
