package fr.univangers.repositories;

import fr.univangers.models.Repertoire;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepertoireRepository extends CrudRepository<Repertoire, Integer> {

}
