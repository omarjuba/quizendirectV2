package fr.univangers.repositories;

import fr.univangers.models.Enseignant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnseignantRepository extends CrudRepository<Enseignant, Integer> {


}
