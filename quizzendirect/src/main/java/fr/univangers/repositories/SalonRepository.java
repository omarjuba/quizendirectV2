package fr.univangers.repositories;

import fr.univangers.models.Salon;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalonRepository extends CrudRepository<Salon, Integer> {
}
