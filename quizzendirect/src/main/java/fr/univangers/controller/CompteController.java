package fr.univangers.controller;

import fr.univangers.User;
import fr.univangers.models.Enseignant;
import fr.univangers.repositories.EnseignantRepository;
import fr.univangers.services.EnseignantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;


@Controller
public class CompteController {

    @Autowired
    private EnseignantService enseignantService;

    @GetMapping("/connection")
    public String connexion(Model model,
                            // récupération des cookies
                            @CookieValue(value="userId_ens",defaultValue = "") String userId_ens,
                            @CookieValue(value="userPrenom",defaultValue = "") String userPrenom,
                            @CookieValue(value="userName",defaultValue = "") String userName,
                            @CookieValue(value="userEmail",defaultValue = "") String userEmail) {
        // test si le cookie existe ou est vide
        if(!userEmail.equals("")) {
            User user = new User();
            if(!userId_ens.equals(""))
                user.setId(Integer.parseInt(userId_ens));
            user.setEmail(userEmail);
            user.setPrenom(userPrenom); user.setName(userName);
            model.addAttribute("user", user);
            return "hubGestion";
        }
        else {
            model.addAttribute("user", new User());
            return "comptePage";
        }
    }
}
