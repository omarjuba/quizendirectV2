package fr.univ.angers.quizz.api.datasfetchers;

import fr.univ.angers.quizz.api.model.Enseignant;
import fr.univ.angers.quizz.api.model.Error;
import fr.univ.angers.quizz.api.model.Question;
import fr.univ.angers.quizz.api.model.Repertoire;
import fr.univ.angers.quizz.api.repository.EnseignantRepository;
import fr.univ.angers.quizz.api.repository.QuestionRepository;
import fr.univ.angers.quizz.api.repository.RepertoireRepository;
import graphql.schema.DataFetcher;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.util.*;

@Component
public class RepertoireDataFetcher {

    @Value("${apiKey}")
    private String apiKey;

    @Autowired
    private RepertoireRepository repertoireRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private EnseignantRepository enseignantRepository;

    //TODO a virer
    public DataFetcher<List<Repertoire>> getAllRepertoire() {
        return dataFetchingEnvironment -> repertoireRepository.findAll();
    }

    public DataFetcher<Object> getRepertoireById() {
        return dataFetchingEnvironment -> {
            Optional<Repertoire> repertoire = repertoireRepository.findById(Integer.parseInt(dataFetchingEnvironment.getArgument("id_rep")));
            if (repertoire.isPresent()){

                if (parseJWT(dataFetchingEnvironment.getArgument("token"),repertoire.get().getEnseignant())) return repertoire.get();
                else return new Error("removeEnseignant", "TOKEN",
                        "Error : TOKEN");
            }
            // Répertoire non trouvé
            return new Error("getRepertoireById", "NOT_FOUND", "Erreur : Aucun répertoire correspondant à l'ID : '" + Integer.parseInt(dataFetchingEnvironment.getArgument("id_rep")) + "' n'a été trouvé.");
        };
    }

    public DataFetcher<Object> createRepertoire() {
        return dataFetchingEnvironment -> {
            // On vérifie que toutes les données passées en paramètres sont valides
            if (StringUtils.normalizeSpace(dataFetchingEnvironment.getArgument("nom")).length() <= 0)
                return new Error("createRepertoire", "INVALID_ARG", "Erreur : Le nom du répertoire que vous avez saisi : '" + dataFetchingEnvironment.getArgument("nom") + "' n'est pas correct.");
            Map<String, Object> enseignantInput = dataFetchingEnvironment.getArgument("enseignant");
            List<Enseignant> enseignants = enseignantRepository.findAll();
            for (Enseignant enseignant : enseignants) {
                if (enseignant.getMail().equals(enseignantInput.get("mail"))) {
                    // On vérifie que le répertoire n'existe pas déjà
                    List<Repertoire> repertoires = repertoireRepository.findAll();
                    for (Repertoire repertoire : repertoires) {
                        if (dataFetchingEnvironment.getArgument("nom").equals(repertoire.getNom()) && enseignant.getId_ens() == repertoire.getEnseignant().getId_ens())
                            return new Error("createRepertoire", "ALREADY_EXISTS", "Erreur : Ce répertoire existe déjà.");
                    }
                    // On crée le nouveau répertoire
                    if (parseJWT(dataFetchingEnvironment.getArgument("token"), enseignant)) {
                        Repertoire nouveauRepertoire = new Repertoire(dataFetchingEnvironment.getArgument("nom"), enseignant);
                        // Sauvegarde dans la base de données
                        repertoireRepository.save(nouveauRepertoire);
                        return nouveauRepertoire;
                    }
                    return new Error("removeEnseignant", "TOKEN",
                            "Error : TOKEN");
                }
            }
            return new Error("createRepertoire", "NOT_FOUND", "Erreur : L'enseignant que vous avez saisi n'existe pas.");
        };
    }

    public DataFetcher<Object> updateRepertoire() {
        return dataFetchingEnvironment -> {
            // On vérifie que le répertoire existe
            Optional<Repertoire> repertoire = repertoireRepository.findById(Integer.parseInt(dataFetchingEnvironment.getArgument("id_rep")));
            if (!repertoire.isPresent())
                return new Error("updateRepertoire", "NOT_FOUND", "Erreur : Aucun répertoire correspondant à l'ID : '" + Integer.parseInt(dataFetchingEnvironment.getArgument("id_rep")) + "' n'a été trouvé.");

            Enseignant ens = repertoire.get().getEnseignant();
            // On modifie les attributs passés en paramètres si les nouvelles valeurs sont valides
            if (parseJWT(dataFetchingEnvironment.getArgument("token"), ens)) {
                if (dataFetchingEnvironment.containsArgument("nom")) {
                    if (StringUtils.normalizeSpace(dataFetchingEnvironment.getArgument("nom")).length() <= 0)
                        return new Error("updateRepertoire", "INVALID_ARG", "Erreur : Le nom du répertoire que vous avez saisi : '" + dataFetchingEnvironment.getArgument("nom") + "' n'est pas correct.");
                    repertoire.get().setNom(dataFetchingEnvironment.getArgument("nom"));
                }
                if (dataFetchingEnvironment.containsArgument("questions")) {
                    // On supprime les questions qui ne font pas partie de la nouvelle liste de questions
                    List<Map<String, Object>> questionsInput = dataFetchingEnvironment.getArgument("questions");
                    List<Question> repertoireQuestions = repertoire.get().getQuestions();
                    List<Question> questionsToRemove = new ArrayList<>();
                    for (Question question : repertoireQuestions) {
                        boolean remove = true;
                        for (Map<String, Object> questionInput : questionsInput) {
                            if (question.getIntitule().equals(questionInput.get("intitule")) && questionInput.get("choixUnique").equals(question.isChoixUnique()) && question.getReponsesBonnes().size() == ((List<String>) questionInput.get("reponsesBonnes")).size() && question.getReponsesFausses().size() == ((List<String>) questionInput.get("reponsesFausses")).size() && questionInput.get("time").equals(question.getTime())) {
                                remove = false;
                                for (int i = 0; i < question.getReponsesBonnes().size(); i++) {
                                    if (!question.getReponsesBonnes().get(i).equals(((List<String>) questionInput.get("reponsesBonnes")).get(i))) {
                                        remove = true;
                                        break;
                                    }
                                }
                                for (int i = 0; i < question.getReponsesFausses().size(); i++) {
                                    if (!question.getReponsesFausses().get(i).equals(((List<String>) questionInput.get("reponsesFausses")).get(i))) {
                                        remove = true;
                                        break;
                                    }
                                }
                                if (remove) continue;
                                break;
                            }
                        }
                        if (remove) questionsToRemove.add(question);
                    }
                    repertoire.get().removeQuestion(questionsToRemove);
                    // On ajoute les nouvelles questions
                    List<Question> questions = questionRepository.findAll();
                    for (Map<String, Object> questionInput : questionsInput) {
                        boolean questionExists = false;
                        for (Question question : questions) {
                            if (question.getIntitule().equals(questionInput.get("intitule")) && questionInput.get("choixUnique").equals(question.isChoixUnique()) && question.getReponsesBonnes().size() == ((List<String>) questionInput.get("reponsesBonnes")).size() && question.getReponsesFausses().size() == ((List<String>) questionInput.get("reponsesFausses")).size() && questionInput.get("time").equals(question.getTime())) {
                                questionExists = true;
                                for (int i = 0; i < question.getReponsesBonnes().size(); i++) {
                                    if (!question.getReponsesBonnes().get(i).equals(((List<String>) questionInput.get("reponsesBonnes")).get(i))) {
                                        questionExists = false;
                                        break;
                                    }
                                }
                                for (int i = 0; i < question.getReponsesFausses().size(); i++) {
                                    if (!question.getReponsesFausses().get(i).equals(((List<String>) questionInput.get("reponsesFausses")).get(i))) {
                                        questionExists = false;
                                        break;
                                    }
                                }
                                if (!questionExists) continue;
                                if (!repertoire.get().getQuestions().contains(question)) {//------------------------------------------------
                                    repertoire.get().addQuestion(question);
                                }
                                break;
                            }
                        }
                        if (!questionExists)
                            return new Error("updateRepertoire", "NOT_FOUND", "Erreur : La question '" + questionInput.get("intitule") + "' n'existe pas.");
                    }
                }
                if (dataFetchingEnvironment.containsArgument("enseignant")) {
                    Map<String, Object> enseignantInput = dataFetchingEnvironment.getArgument("enseignant");
                    List<Enseignant> enseignants = enseignantRepository.findAll();
                    boolean enseignantFound = false;
                    for (Enseignant enseignant : enseignants) {
                        if (enseignant.getMail().equals(enseignantInput.get("mail"))) {
                            repertoire.get().setEnseignant(enseignant);
                            enseignantFound = true;
                            break;
                        }
                    }
                    if (!enseignantFound)
                        return new Error("updateRepertoire", "NOT_FOUND", "Erreur : L'enseignant '" + enseignantInput.get("mail") + "' n'existe pas.");
                }

                // Sauvegarde dans la base de données
                repertoireRepository.save(repertoire.get());
                return repertoire;
            }
            return new Error("removeEnseignant", "TOKEN",
                    "Error : TOKEN");
        };
    }

    public DataFetcher<Object> removeRepertoire() {
        return dataFetchingEnvironment -> {
            // On vérifie que le répertoire existe
            Optional<Repertoire> repertoire = repertoireRepository.findById(Integer.parseInt(dataFetchingEnvironment.getArgument("id_rep")));
            if (!repertoire.isPresent())
                return new Error("removeRepertoire", "NOT_FOUND", "Erreur : Aucun répertoire correspondant à l'ID : '" + Integer.parseInt(dataFetchingEnvironment.getArgument("id_rep")) + "' n'a été trouvé.");

            // Suppression du répertoire de la base de données
            Enseignant enseignant = repertoire.get().getEnseignant();

            if (parseJWT(dataFetchingEnvironment.getArgument("token"), enseignant)) {
                repertoireRepository.delete(repertoire.get());
                return repertoire;
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
