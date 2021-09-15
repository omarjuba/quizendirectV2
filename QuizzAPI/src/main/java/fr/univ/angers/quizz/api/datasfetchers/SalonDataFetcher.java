package fr.univ.angers.quizz.api.datasfetchers;

import fr.univ.angers.quizz.api.model.*;
import fr.univ.angers.quizz.api.model.Error;
import fr.univ.angers.quizz.api.repository.EnseignantRepository;
import fr.univ.angers.quizz.api.repository.EtudiantRepository;
import fr.univ.angers.quizz.api.repository.QuestionRepository;
import fr.univ.angers.quizz.api.repository.SalonRepository;
import graphql.schema.DataFetcher;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import javax.swing.text.html.parser.Entity;
import javax.xml.bind.DatatypeConverter;
import java.util.*;

@Component
public class SalonDataFetcher {

    @Value("${apiKey}")
    private String apiKey;

    @Autowired
    private SalonRepository salonRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private EtudiantRepository etudiantRepository;
    @Autowired
    private EnseignantRepository enseignantRepository;

    public DataFetcher<List<Salon>> getAllSalon() {
        return dataFetchingEnvironment -> salonRepository.findAll();
    }

    public DataFetcher<Object> getSalonById() {
        return dataFetchingEnvironment -> {
            Optional<Salon> salon = salonRepository.findById(Integer.parseInt(dataFetchingEnvironment.getArgument("id_salon")));
            if (salon.isPresent()) {
                if (parseJWT(dataFetchingEnvironment.getArgument("token"), salon.get().getEnseignant()))
                    return salon.get();
                else return new Error("removeEnseignant", "TOKEN",
                        "Error : TOKEN");
            }
            // Salon non trouvé
            return new Error("getSalonById", "NOT_FOUND", "Erreur : Aucun salon correspondant à l'ID : '" + Integer.parseInt(dataFetchingEnvironment.getArgument("id_salon")) + "' n'a été trouvé.");
        };
    }

    public DataFetcher<Object> createSalon() {
        return dataFetchingEnvironment -> {
            // On vérifie que toutes les données passées en paramètres sont valides
            if (((int) dataFetchingEnvironment.getArgument("codeAcces")) <= 0)
                return new Error("createSalon", "INVALID_ARG", "Erreur : Le code d'accès du salon que vous avez saisi : '" + dataFetchingEnvironment.getArgument("codeAcces") + "' n'est pas correct.");
            Map<String, Object> enseignantInput = dataFetchingEnvironment.getArgument("enseignant");
            List<Enseignant> enseignants = enseignantRepository.findAll();
            for (Enseignant enseignant : enseignants) {
                if (enseignantInput.get("mail").equals(enseignant.getMail())) {
                    // On vérifie que le salon n'existe pas déjà
                    List<Salon> salons = salonRepository.findAll();
                    for (Salon salon : salons) {
                        if (dataFetchingEnvironment.getArgument("codeAcces").equals(salon.getCodeAcces()))
                            return new Error("createSalon", "ALREADY_EXISTS", "Erreur : Ce salon existe déjà.");
                    }
                    // On crée le nouveau salon
                    if (parseJWT(dataFetchingEnvironment.getArgument("token"), enseignant)) {
                        Salon nouveauSalon = new Salon(dataFetchingEnvironment.getArgument("codeAcces"), enseignant);
                        salonRepository.save(nouveauSalon);
                        // Sauvegarde dans la base de données
                        return nouveauSalon;
                    }
                    return new Error("removeEnseignant", "TOKEN",
                            "Error : TOKEN");
                }
            }
            return new Error("createSalon", "NOT_FOUND", "Erreur : L'enseignant que vous avez saisi n'existe pas.");
        };
    }

    public DataFetcher<Object> updateSalon() {
        return dataFetchingEnvironment -> {
            // On vérifie que le salon existe
            Optional<Salon> salon = salonRepository.findById(Integer.parseInt(dataFetchingEnvironment.getArgument("id_salon")));
            if (!salon.isPresent())
                return new Error("updateSalon", "NOT_FOUND", "Erreur : Aucun salon correspondant à l'ID : '" + Integer.parseInt(dataFetchingEnvironment.getArgument("id_salon")) + "' n'a été trouvé.");

            if (parseJWT(dataFetchingEnvironment.getArgument("token"), salon.get().getEnseignant())) {
                // On modifie les attributs passés en paramètres si les nouvelles valeurs sont valides
                if (dataFetchingEnvironment.containsArgument("codeAcces")) {
                    if (((int) dataFetchingEnvironment.getArgument("codeAcces")) <= 0)
                        return new Error("updateSalon", "INVALID_ARG", "Erreur : Le code d'accès du salon que vous avez saisi : '" + dataFetchingEnvironment.getArgument("codeAcces") + "' n'est pas correct.");
                    salon.get().setCodeAcces(dataFetchingEnvironment.getArgument("codeAcces"));
                }
                if (dataFetchingEnvironment.containsArgument("questionsEnAttente")) {
                    List<Map<String, Object>> questionsInput = dataFetchingEnvironment.getArgument("questionsEnAttente");
                    List<Question> questions = questionRepository.findAll();
                    List<Question> questionsToAdd = new ArrayList<Question>();
                    for (Map<String, Object> questionInput : questionsInput) {
                        boolean questionFound = false;
                        for (Question question : questions) {
                            if (question.getIntitule().equals(questionInput.get("intitule")) && questionInput.get("choixUnique").equals(question.isChoixUnique()) && question.getReponsesBonnes().size() == ((List<String>) questionInput.get("reponsesBonnes")).size() && question.getReponsesFausses().size() == ((List<String>) questionInput.get("reponsesFausses")).size() && questionInput.get("time").equals(question.getTime())) {
                                questionFound = true;
                                for (int i = 0; i < question.getReponsesBonnes().size(); i++) {
                                    if (!question.getReponsesBonnes().get(i).equals(((List<String>) questionInput.get("reponsesBonnes")).get(i))) {
                                        questionFound = false;
                                        break;
                                    }
                                }
                                for (int i = 0; i < question.getReponsesFausses().size(); i++) {
                                    if (!question.getReponsesFausses().get(i).equals(((List<String>) questionInput.get("reponsesFausses")).get(i))) {
                                        questionFound = false;
                                        break;
                                    }
                                }
                                if (!questionFound) continue;
                                questionsToAdd.add(question);
                                break;
                            }
                        }
                        if (!questionFound)
                            return new Error("updateSalon", "NOT_FOUND", "Erreur :  La question '" + questionInput.get("intitule") + "' n'existe pas.");
                    }
                    salon.get().setQuestionsEnAttente(questionsToAdd);
                }
                if (dataFetchingEnvironment.containsArgument("questionsPosees")) {
                    List<Map<String, Object>> questionsInput = dataFetchingEnvironment.getArgument("questionsPosees");
                    List<Question> questions = questionRepository.findAll();
                    List<Question> questionsToAdd = new ArrayList<Question>();
                    for (Map<String, Object> questionInput : questionsInput) {
                        boolean questionFound = false;
                        for (Question question : questions) {
                            if (question.getIntitule().equals(questionInput.get("intitule")) && questionInput.get("choixUnique").equals(question.isChoixUnique()) && question.getReponsesBonnes().size() == ((List<String>) questionInput.get("reponsesBonnes")).size() && question.getReponsesFausses().size() == ((List<String>) questionInput.get("reponsesFausses")).size() && questionInput.get("time").equals(question.getTime())) {
                                questionFound = true;
                                for (int i = 0; i < question.getReponsesBonnes().size(); i++) {
                                    if (!question.getReponsesBonnes().get(i).equals(((List<String>) questionInput.get("reponsesBonnes")).get(i))) {
                                        questionFound = false;
                                        break;
                                    }
                                }
                                for (int i = 0; i < question.getReponsesFausses().size(); i++) {
                                    if (!question.getReponsesFausses().get(i).equals(((List<String>) questionInput.get("reponsesFausses")).get(i))) {
                                        questionFound = false;
                                        break;
                                    }
                                }
                                if (!questionFound) continue;
                                questionsToAdd.add(question);
                                break;
                            }
                        }
                        if (!questionFound)
                            return new Error("updateSalon", "NOT_FOUND", "Erreur :  La question '" + questionInput.get("intitule") + "' n'existe pas.");
                    }
                    salon.get().setQuestionsPosees(questionsToAdd);
                }
                if (dataFetchingEnvironment.containsArgument("questionCourante")) {
                    Map<String, Object> questionInput = dataFetchingEnvironment.getArgument("questionCourante");
                    List<Question> questions = questionRepository.findAll();
                    boolean questionFound = false;
                    for (Question question : questions) {
                        if (question.getIntitule().equals(questionInput.get("intitule")) && questionInput.get("choixUnique").equals(question.isChoixUnique()) && question.getReponsesBonnes().size() == ((List<String>) questionInput.get("reponsesBonnes")).size() && question.getReponsesFausses().size() == ((List<String>) questionInput.get("reponsesFausses")).size() && questionInput.get("time").equals(question.getTime())) {
                            questionFound = true;
                            for (int i = 0; i < question.getReponsesBonnes().size(); i++) {
                                if (!question.getReponsesBonnes().get(i).equals(((List<String>) questionInput.get("reponsesBonnes")).get(i))) {
                                    questionFound = false;
                                    break;
                                }
                            }
                            for (int i = 0; i < question.getReponsesFausses().size(); i++) {
                                if (!question.getReponsesFausses().get(i).equals(((List<String>) questionInput.get("reponsesFausses")).get(i))) {
                                    questionFound = false;
                                    break;
                                }
                            }
                            if (!questionFound) continue;
                            salon.get().setQuestionCourante(question);
                            break;
                        }
                    }
                    if (!questionFound)
                        return new Error("updateSalon", "NOT_FOUND", "Erreur :  La question '" + questionInput.get("intitule") + "' n'existe pas.");
                }
                if (dataFetchingEnvironment.containsArgument("etudiants")) {
                    // On supprime les étudiants qui ne font pas partie de la nouvelle liste des étudiants
                    List<Map<String, Object>> etudiantsInput = dataFetchingEnvironment.getArgument("etudiants");
                    List<Etudiant> salonEtudiants = salon.get().getEtudiants();
                    List<Etudiant> etudiantsToRemove = new ArrayList<>();
                    for (Etudiant etudiant : salonEtudiants) {
                        boolean remove = true;
                        for (Map<String, Object> etudiantInput : etudiantsInput) {
                            Map<String, Object> salonInput = (Map<String, Object>) etudiantInput.get("salon");
                            if (etudiant.getPseudo().equals(etudiantInput.get("pseudo")) && (salonInput.get("codeAcces").equals(etudiant.getSalon().getCodeAcces()))) {
                                remove = false;
                                break;
                            }
                        }
                        if (remove) etudiantsToRemove.add(etudiant);
                        if (salon.get().getEtudiants().isEmpty()) break;
                    }
                    salon.get().removeEtudiants(etudiantsToRemove);
                    // On ajoute les nouveaux étudiants
                    List<Etudiant> etudiants = etudiantRepository.findAll();
                    for (Map<String, Object> etudiantInput : etudiantsInput) {
                        boolean etudiantExists = false;
                        Map<String, Object> salonInput = (Map<String, Object>) etudiantInput.get("salon");
                        for (Etudiant etudiant : etudiants) {
                            if (etudiant.getPseudo().equals(etudiantInput.get("pseudo")) && salonInput.get("codeAcces").equals(etudiant.getSalon().getCodeAcces())) {
                                if (!salon.get().getEtudiants().contains(etudiant)) {//------------------------------------------------
                                    salon.get().addEtudiants(etudiant);
                                }
                                etudiantExists = true;
                                break;
                            }
                        }
                        if (!etudiantExists)
                            return new Error("updateSalon", "NOT_FOUND", "Erreur : L'étudiant '" + etudiantInput.get("pseudo") + "' n'existe pas.");
                    }
                }
                if (dataFetchingEnvironment.containsArgument("enseignant")) {
                    Map<String, Object> enseignantInput = dataFetchingEnvironment.getArgument("enseignant");
                    List<Enseignant> enseignants = enseignantRepository.findAll();
                    boolean enseignantFound = false;
                    for (Enseignant enseignant : enseignants) {
                        if (enseignantInput.get("mail").equals(enseignant.getMail())) {
                            salon.get().setEnseignant(enseignant);
                            enseignantFound = true;
                            break;
                        }
                    }
                    if (!enseignantFound)
                        return new Error("updateSalon", "NOT_FOUND", "Erreur :  L'enseignant '" + enseignantInput.get("mail") + "' n'existe pas.");
                }

                // Sauvegarde dans la base de données
                salonRepository.save(salon.get());
                return salon;
            }
            return new Error("removeEnseignant", "TOKEN",
                    "Error : TOKEN");
        };
    }

    public DataFetcher<Object> removeSalon() {
        return dataFetchingEnvironment -> {
            // On vérifie que le salon existe
            Optional<Salon> salon = salonRepository.findById(Integer.parseInt(dataFetchingEnvironment.getArgument("id_salon")));
            if (!salon.isPresent())
                return new Error("removeSalon", "NOT_FOUND", "Erreur : Aucun salon correspondant à l'ID : '" + Integer.parseInt(dataFetchingEnvironment.getArgument("id_salon")) + "' n'a été trouvé.");

            // Suppression du salon de la base de données
            if (parseJWT(dataFetchingEnvironment.getArgument("token"), salon.get().getEnseignant())) {
                salonRepository.delete(salon.get());
                return salon;
            }
            return new Error("removeEnseignant", "TOKEN",
                    "Error : TOKEN");
        };
    }

    //Sample method to validate and read the JWT
    private boolean parseJWT(String jwt, Enseignant enseignant) {

        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(apiKey))
                .parseClaimsJws(jwt).getBody();
        if (claims.getId().equals(String.valueOf(enseignant.getId_ens()))) {
            if (claims.getSubject().equals(enseignant.getNom())) {
                Date datenow = new Date(System.currentTimeMillis());
                if (claims.getExpiration().after(datenow)) {
                    return true;
                }
            }
        }
        return false;
    }
}
