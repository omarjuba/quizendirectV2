package fr.univ.angers.quizz.api.repository;

import fr.univ.angers.quizz.api.model.Repertoire;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepertoireRepository extends CrudRepository<Repertoire, Integer> {
    public List<Repertoire> findAll();
}
