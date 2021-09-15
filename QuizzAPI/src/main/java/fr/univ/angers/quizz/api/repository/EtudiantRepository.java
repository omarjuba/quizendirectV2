package fr.univ.angers.quizz.api.repository;

import fr.univ.angers.quizz.api.model.Etudiant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EtudiantRepository extends CrudRepository<Etudiant, Integer> {
    public List<Etudiant> findAll();
}
