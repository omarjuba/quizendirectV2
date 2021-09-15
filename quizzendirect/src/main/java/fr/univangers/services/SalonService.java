package fr.univangers.services;

import fr.univangers.models.Salon;
import fr.univangers.repositories.SalonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SalonService {

    @Autowired
    private SalonRepository salonRepository;

    public ArrayList<Salon> getAllSalons(){
        ArrayList<Salon> salons = new ArrayList<Salon>();
        salonRepository.findAll().forEach(salons::add);
        return salons;
    }

    public Salon getSalon(int id_salon) { return salonRepository.findById(id_salon).orElse(new Salon());}

    public void addSalon(Salon salon) { salonRepository.save(salon);}

    public void updateSalon(Salon salon) { salonRepository.save(salon);}

    public void removeSalon(int id_salon) { salonRepository.deleteById(id_salon);}
}
