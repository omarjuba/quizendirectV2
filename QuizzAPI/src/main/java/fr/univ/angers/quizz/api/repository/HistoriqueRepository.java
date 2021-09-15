package fr.univ.angers.quizz.api.repository;

import fr.univ.angers.quizz.api.model.Historique;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoriqueRepository extends CrudRepository<Historique, Integer> {
    public List<Historique> findAll();
}
