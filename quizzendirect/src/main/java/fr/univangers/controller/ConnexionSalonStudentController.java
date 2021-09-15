
package fr.univangers.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.util.ArrayList;

@Controller
public class ConnexionSalonStudentController {

    @GetMapping("/ConnexionSalonStudent")
    public String getConnexionSalonStudent(){
        return "ConnexionSallonStudent";
    }

    @PostMapping("/ConnexionSalonStudent")
    public String getConnexionSalonStudent(Model model, HttpServletRequest request){

        // pour le moment, comme on ne sait pas ce qu'est un salon, on a choisi d'en faire une classe interne
        // attributs : nom et liste de questions
        class Salon{
            public String nom;
            public ArrayList<String> questions;

            Salon(String nom,ArrayList<String> questions) {
                this.nom = nom;
                this.questions = questions;
            }
        };

        // création des salons ( pour le moment créés à la main )
        Salon salon1 = new Salon("abcd",
            new ArrayList<String>(){
                {
                    add("A hérite de B es-ce que A a les attributs de B ?");
                    add("C++ est-il un langage Orienté Objet ?");
                }
            });
        Salon salon2 = new Salon("efgh",
            new ArrayList<String>(){
                {
                    add("Spring est-il un framework?");
                    add("Le constructeur par recopie est-il obligatoire en C++?");
                }
            });

        // récupérations des saisies
        String pseudo = request.getParameter("pseudo");
        String idSalon = request.getParameter("idSalon");

        // gestion des saisies
        if(idSalon.equals(salon1.nom) || idSalon.equals(salon2.nom)) { // si le salon saisi n'est pas valide
            // récupération de la partie de QuizController car on fera appel à la page quiz
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

            // ajout des questions à l'objet info.questions
            if(idSalon.equals(salon1.nom))
                for (int i = 0 ; i<salon1.questions.size();i++) info.questions.add(salon1.questions.get(i));
            else
                for (int i = 0 ; i<salon2.questions.size();i++) info.questions.add(salon2.questions.get(i));

            // ajout des réponses
            info.reponses.add("Oui");
            info.reponses.add("Non");
            info.reponses.add("D'accord");
            info.reponses.add("Possible");
            info.reponses.add("Pas possible");
            info.reponses.add("Impossible");

            model.addAttribute("pseudo",pseudo);
            model.addAttribute("info",info);
            return "quiz";
        }
        else {
            model.addAttribute("erreur","Mauvais salon en entrée");
            return "ConnexionSallonStudent";
        }
    }





}
