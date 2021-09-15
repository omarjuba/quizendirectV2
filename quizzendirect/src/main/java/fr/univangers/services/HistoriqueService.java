package fr.univangers.services;

import fr.univangers.models.Historique;
import fr.univangers.repositories.HistoriqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class HistoriqueService {

    @Autowired
    private HistoriqueRepository historiqueRepository;

    public ArrayList<Historique> getAllHistoriques(){
        ArrayList<Historique> historiques = new ArrayList<>();
        historiqueRepository.findAll().forEach(historiques::add);
        return historiques;
    }

    public Historique getHistorique(int id_quest){
        return historiqueRepository.findById(id_quest).orElse(new Historique());
    }

    public void addHistorique(Historique historique){ historiqueRepository.save(historique); }

    public void updateHistorique(Historique historique){
        historiqueRepository.save(historique);
    }

    public void removeHistorique(int id_quest){
        historiqueRepository.deleteById(id_quest);
    }
}
