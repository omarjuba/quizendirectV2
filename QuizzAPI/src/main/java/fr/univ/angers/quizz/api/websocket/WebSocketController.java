package fr.univ.angers.quizz.api.websocket;

import fr.univ.angers.quizz.api.model.Question;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private Logger logger = LogManager.getLogger(WebSocketController.class);

    @MessageMapping("/salon/{codeAcces}")
    @SendTo("/quiz/salon/{codeAcces}")
    public Question getCurrentQuestion(Question question,@DestinationVariable Integer codeAcces) {
        logger.info("WebSocketController : createSalon()");
        System.out.println("-----------------------------------------");
        System.out.println(question.isChoixUnique());
        System.out.println("---------------------------------------");
        return question;
    }
    
    @MessageMapping("/gettype/{codeAcces}")
    @SendTo("/quiz/salon/gettype/{codeAcces}")
    public int getMessage(int typeofquestion,@DestinationVariable Integer codeAcces) {
        return typeofquestion;
    }

    @MessageMapping("/closed/{codeAcces}")
    @SendTo("/quiz/salon/closed/{codeAcces}")
    public String getMessage(String text,@DestinationVariable Integer codeAcces) {
        return text;
    }
}
