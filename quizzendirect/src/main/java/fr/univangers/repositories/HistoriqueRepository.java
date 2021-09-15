package fr.univangers.repositories;

import fr.univangers.models.Historique;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoriqueRepository extends CrudRepository<Historique, Integer> {

}
