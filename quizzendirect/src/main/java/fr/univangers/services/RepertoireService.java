package fr.univangers.services;

import fr.univangers.models.Repertoire;
import fr.univangers.repositories.RepertoireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class RepertoireService {

    @Autowired
    private RepertoireRepository repertoireRepository;

    public ArrayList<Repertoire> getAllRepertoires(){
        ArrayList<Repertoire> repertoires = new ArrayList<>();
        repertoireRepository.findAll().forEach(repertoires::add);
        return repertoires;
    }

    public Repertoire getRepertoire(int id_rep){ return repertoireRepository.findById(id_rep).orElse(new Repertoire()); }

    public void addRepertoire(Repertoire repertoire){
        repertoireRepository.save(repertoire);
    }

    public void updateRepertoire(Repertoire repertoire){
        repertoireRepository.save(repertoire);
    }

    public void removeRepertoire(int id_rep){
        repertoireRepository.deleteById(id_rep);
    }
}
