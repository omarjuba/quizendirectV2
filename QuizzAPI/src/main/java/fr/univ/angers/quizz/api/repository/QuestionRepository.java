package fr.univ.angers.quizz.api.repository;

import fr.univ.angers.quizz.api.model.Question;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Integer> {
    public List<Question> findAll();
    public Question findQuestionByIntitule(String intitule);
}
