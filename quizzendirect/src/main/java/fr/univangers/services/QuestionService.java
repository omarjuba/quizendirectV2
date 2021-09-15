package fr.univangers.services;

import fr.univangers.models.Question;
import fr.univangers.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public ArrayList<Question> getAllQuestions(){
        ArrayList<Question> questions = new ArrayList<>();
        questionRepository.findAll().forEach(questions::add);
        return questions;
    }

    public Question getQuestion(int id_quest){
        return questionRepository.findById(id_quest).orElse(new Question());
    }

    public void addQuestion(Question question){
        questionRepository.save(question);
    }

    public void updateQuestion(Question question){
        questionRepository.save(question);
    }

    public void removeQuestion(int id_quest){
        questionRepository.deleteById(id_quest);
    }
}
