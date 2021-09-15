
package fr.univangers.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SelectUserController {
    @GetMapping("/SelectPage")
    public String getSelectedPage(){
            return "home";
    }
}