package fr.univangers.services;

import fr.univangers.models.Enseignant;
import fr.univangers.repositories.EnseignantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class EnseignantService {

    @Autowired
    private EnseignantRepository enseignantRepository;

    public ArrayList<Enseignant> getAllEnseignants(){
        ArrayList<Enseignant> enseignants = new ArrayList<>();
        enseignantRepository.findAll().forEach(enseignants::add);
        return enseignants;
    }

    public Enseignant getEnseignant(int id_ens) {
        return enseignantRepository.findById(id_ens).orElse(new Enseignant());
    }

    public void addEnseignant(Enseignant enseignant){
        enseignantRepository.save(enseignant);
    }

    public void updateEnseignant(Enseignant enseignant){
        enseignantRepository.save(enseignant);
    }

    public void removeEnseignant(int id_ens){
        enseignantRepository.deleteById(id_ens);
    }
}
