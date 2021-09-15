package fr.univ.angers.quizz.api.datasfetchers;

import fr.univ.angers.quizz.api.model.Enseignant;
import fr.univ.angers.quizz.api.model.Error;
import fr.univ.angers.quizz.api.model.Etudiant;
import fr.univ.angers.quizz.api.model.Salon;
import fr.univ.angers.quizz.api.repository.EtudiantRepository;
import fr.univ.angers.quizz.api.repository.SalonRepository;
import graphql.execution.DataFetcherResult;
import graphql.schema.DataFetcher;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class EtudiantDataFetcher {

    @Autowired
    private EtudiantRepository etudiantRepository;
    @Autowired
    private SalonRepository salonRepository;

    public DataFetcher<List<Etudiant>> getAllEtudiant(){
        return dataFetchingEnvironment -> etudiantRepository.findAll();
    }

    public DataFetcher<Object> getEtudiantById(){
        return dataFetchingEnvironment -> {
            Optional<Etudiant> etudiant = etudiantRepository.findById(Integer.parseInt(dataFetchingEnvironment.getArgument("id_etud")));
            if(etudiant.isPresent()) return etudiant.get();
            // Étudiant non trouvé
            return new Error("getEtudiantById", "NOT_FOUND", "Erreur : Aucun étudiant correspondant à l'ID : '" + Integer.parseInt(dataFetchingEnvironment.getArgument("id_etud")) +  "' n'a été trouvé.");
        };
    }

    public DataFetcher<Object> createEtudiant(){
        return dataFetchingEnvironment -> {
            // On vérifie que toutes les données passées en paramètres sont valides
            if(StringUtils.normalizeSpace(dataFetchingEnvironment.getArgument("pseudo")).length() <= 0) return new Error("createEtudiant", "INVALID_ARG", "Erreur : Le pseudo de l'étudiant que vous avez saisi : '" + dataFetchingEnvironment.getArgument("pseudo") +  "' n'est pas correct.");
            Map<String, Object> salonInput = dataFetchingEnvironment.getArgument("salon");
            List<Salon> salons = salonRepository.findAll();
            for(Salon salon : salons){
                if(salonInput.get("codeAcces").equals(salon.getCodeAcces())) {
                    // On vérifie que l'étudiant n'existe pas déjà
                    List<Etudiant> etudiants = etudiantRepository.findAll();
                    for(Etudiant etudiant : etudiants) {
                        if (dataFetchingEnvironment.getArgument("pseudo").equals(etudiant.getPseudo()) && etudiant.getSalon().getCodeAcces()  == salon.getCodeAcces()) return new Error("createEtudiant", "ALREADY_EXISTS", "Erreur : Cet étudiant existe déjà.");
                    }
                    // On crée le nouvel étudiant
                    Etudiant nouvelEtudiant = new Etudiant(dataFetchingEnvironment.getArgument("pseudo"), salon);
                    // Sauvegarde dans la base de données
                    etudiantRepository.save(nouvelEtudiant);
                    return nouvelEtudiant;
                }
            }
            return new Error("createEtudiant", "NOT_FOUND", "Erreur : Aucun salon correspondant au code : '" + salonInput.get("codeAcces") +  "' n'a été trouvé.");
        };
    }

    public DataFetcher<Object> updateEtudiant(){
        return dataFetchingEnvironment -> {
            // On vérifie que l'étudiant existe
            Optional<Etudiant> etudiant = etudiantRepository.findById(Integer.parseInt(dataFetchingEnvironment.getArgument("id_etud")));
            if(!etudiant.isPresent()) return new Error("updateEtudiant",  "NOT_FOUND", "Erreur : Aucun étudiant correspondant à l'ID : '" + Integer.parseInt(dataFetchingEnvironment.getArgument("id_etud")) +  "' n'a été trouvé.");

            // On modifie les attributs passés en paramètres si les nouvelles valeurs sont valides
            if(dataFetchingEnvironment.containsArgument("pseudo")) {
                if(StringUtils.normalizeSpace(dataFetchingEnvironment.getArgument("pseudo")).length() <= 0) return new Error("updateEtudiant", "INVALID_ARG", "Erreur : Le pseudo de l'étudiant que vous avez saisi : '" + dataFetchingEnvironment.getArgument("pseudo") +  "' n'est pas correct.");
                etudiant.get().setPseudo(dataFetchingEnvironment.getArgument("pseudo"));
            }
            if(dataFetchingEnvironment.containsArgument("salon")) {
                Map<String, Object> salonInput = dataFetchingEnvironment.getArgument("salon");
                List<Salon> salons = salonRepository.findAll();
                boolean salonFound = false;
                for(Salon salon : salons) {
                    if (salonInput.get("codeAcces").equals(salon.getCodeAcces())) {
                        etudiant.get().setSalon(salon);
                        salonFound = true;
                        break;
                    }
                }
                if(!salonFound) return new Error("updateEtudiant", "NOT_FOUND", "Erreur : Le salon '" + salonInput.get("codeAcces") + "' n'existe pas.");
            }
            if(dataFetchingEnvironment.containsArgument("bonnesReponses")) {
                if(((int) dataFetchingEnvironment.getArgument("bonnesReponses")) < 0) return new Error("updateEtudiant", "INVALID_ARG", "Erreur : Le nombre de bonnes réponses que vous avez saisi : '" + dataFetchingEnvironment.getArgument("bonnesReponses") +  "' n'est pas correct.");
                etudiant.get().setBonnesReponses(dataFetchingEnvironment.getArgument("bonnesReponses"));
            }
            if(dataFetchingEnvironment.containsArgument("questionsRepondues")) {
                if(((int) dataFetchingEnvironment.getArgument("questionsRepondues")) < 0) return new Error("updateEtudiant", "INVALID_ARG", "Erreur : Le nombre de questions répondues que vous avez saisi : '" + dataFetchingEnvironment.getArgument("questionsRepondues") +  "' n'est pas correct.");
                etudiant.get().setQuestionsRepondues(dataFetchingEnvironment.getArgument("questionsRepondues"));
            }

            // Sauvegarde dans la base de données
            etudiantRepository.save(etudiant.get());
            return etudiant;
        };
    }

    public DataFetcher<Object> sendReponse(){
        return dataFetchingEnvironment -> {
            Optional<Etudiant> etudiant = etudiantRepository.findById(Integer.parseInt(dataFetchingEnvironment.getArgument("id_etud")));
            if(!etudiant.isPresent()) return new Error("updateEtudiant",  "NOT_FOUND", "Erreur : Aucun étudiant correspondant à l'ID : '" + Integer.parseInt(dataFetchingEnvironment.getArgument("id_etud")) +  "' n'a été trouvé.");


            etudiantRepository.save(etudiant.get());
            return etudiant;
        };
    }

    public DataFetcher<Object> removeEtudiant(){
        return dataFetchingEnvironment -> {
            // On vérifie que l'étudiant existe
            Optional<Etudiant> etudiant = etudiantRepository.findById(Integer.parseInt(dataFetchingEnvironment.getArgument("id_etud")));
            if(!etudiant.isPresent()) return new Error("removeEtudiant", "NOT_FOUND", "Erreur : Aucun étudiant correspondant à l'ID : '" + Integer.parseInt(dataFetchingEnvironment.getArgument("id_etud")) + "' n'a été trouvé.");

            // Suppression de l'étudiant de la base de données
            etudiantRepository.delete(etudiant.get());
            return etudiant;
        };
    }
}
