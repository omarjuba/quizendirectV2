package fr.univ.angers.quizz.api.repository;

import fr.univ.angers.quizz.api.model.Salon;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalonRepository extends CrudRepository<Salon, Integer> {
    public List<Salon> findAll();
}
