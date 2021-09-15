package fr.univ.angers.quizz.api.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import fr.univ.angers.quizz.api.model.Enseignant;
import org.springframework.stereotype.Repository;

@Repository
public interface EnseignantRepository extends CrudRepository<Enseignant, Integer> {

    /**
     * @return
     */
    @Override
    public List<Enseignant> findAll();
    Enseignant findByMail(String mail);
}

