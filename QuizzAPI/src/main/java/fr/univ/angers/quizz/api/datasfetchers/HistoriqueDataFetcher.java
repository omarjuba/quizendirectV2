package fr.univ.angers.quizz.api.datasfetchers;

import fr.univ.angers.quizz.api.model.Enseignant;
import fr.univ.angers.quizz.api.model.Error;
import fr.univ.angers.quizz.api.model.Historique;
import fr.univ.angers.quizz.api.model.Question;
import fr.univ.angers.quizz.api.repository.HistoriqueRepository;
import fr.univ.angers.quizz.api.repository.QuestionRepository;
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
public class HistoriqueDataFetcher {

    @Value("${apiKey}")
    private String apiKey;

    @Autowired
    private HistoriqueRepository historiqueRepository;
    @Autowired
    private QuestionRepository questionRepository;

    public DataFetcher<List<Historique>> getAllHistorique() {
        return dataFetchingEnvironment -> historiqueRepository.findAll();
    }

    public DataFetcher<Object> getHistoriqueById() {
        return dataFetchingEnvironment -> {
            Optional<Historique> historique = historiqueRepository.findById(Integer.parseInt(dataFetchingEnvironment.getArgument("id_hist")));
            if (historique.isPresent()) return historique.get();
            // Historique non trouvé
            return new Error("getHistoriqueById", "NOT_FOUND", "Erreur : Aucun historique correspondant à l'ID : '" + Integer.parseInt(dataFetchingEnvironment.getArgument("id_hist")) + "' n'a été trouvé.");
        };
    }

    public DataFetcher<Object> createHistorique() {
        return dataFetchingEnvironment -> {
            // On vérifie que toutes les données passées en paramètres sont valides
            if (StringUtils.normalizeSpace(dataFetchingEnvironment.getArgument("date")).length() <= 0)
                return new Error("createHistorique", "INVALID_ARG", "Erreur : La date de l'historique que vous avez saisi : '" + dataFetchingEnvironment.getArgument("date") + "' n'est pas correcte.");
            Map<String, Object> questionInput = dataFetchingEnvironment.getArgument("question");
            List<Question> questions = questionRepository.findAll();
            for (Question question : questions) {
                if (question.getIntitule().equals(questionInput.get("intitule")) && questionInput.get("choixUnique").equals(question.isChoixUnique()) && question.getReponsesBonnes().size() == ((List<String>) questionInput.get("reponsesBonnes")).size() && question.getReponsesFausses().size() == ((List<String>) questionInput.get("reponsesFausses")).size() && questionInput.get("time").equals(question.getTime())) {
                    boolean notFound = false;
                    for (int i = 0; i < question.getReponsesBonnes().size(); i++) {
                        if (!question.getReponsesBonnes().get(i).equals(((List<String>) questionInput.get("reponsesBonnes")).get(i))) {
                            notFound = true;
                            break;
                        }
                    }
                    for (int i = 0; i < question.getReponsesFausses().size(); i++) {
                        if (!question.getReponsesFausses().get(i).equals(((List<String>) questionInput.get("reponsesFausses")).get(i))) {
                            notFound = true;
                            break;
                        }
                    }
                    if (notFound) continue;
                    // On vérifie que l'historique n'existe pas déjà
                    List<Historique> historiques = historiqueRepository.findAll();
                    for (Historique historique : historiques) {
                        if (historique.getQuestion().getId_quest() == question.getId_quest() && dataFetchingEnvironment.getArgument("date").equals(historique.getDate()))
                            return new Error("createHistorique", "ALREADY_EXISTS", "Erreur : Un historique correspondant à cette question et cette date existe déjà.");
                    }
                    // On crée le nouvel historique
                    Historique nouvelHistorique = new Historique(question, dataFetchingEnvironment.getArgument("date"));
                    // Sauvegarde dans la base de données
                    historiqueRepository.save(nouvelHistorique);
                    return nouvelHistorique;
                }
            }
            return new Error("createHistorique", "NOT_FOUND", "Erreur : La question '" + questionInput.get("intitule") + "' n'existe pas.");
        };
    }

    public DataFetcher<Object> updateHistorique() {
        return dataFetchingEnvironment -> {
            // On vérifie que l'historique existe
            Optional<Historique> historique = historiqueRepository.findById(Integer.parseInt(dataFetchingEnvironment.getArgument("id_hist")));
            if (!historique.isPresent())
                return new Error("updateHistorique", "NOT_FOUND", "Erreur : Aucun historique correspondant à l'ID : '" + Integer.parseInt(dataFetchingEnvironment.getArgument("id_hist")) + "' n'a été trouvé.");

            if (parseJWT(dataFetchingEnvironment.getArgument("token"))) {
                // On modifie les attributs passés en paramètres si les nouvelles valeurs sont valides
                if (dataFetchingEnvironment.containsArgument("question")) {
                    Map<String, Object> questionInput = dataFetchingEnvironment.getArgument("question");
                    List<Question> questions = questionRepository.findAll();
                    boolean notFound = true;
                    for (Question question : questions) {
                        if (question.getIntitule().equals(questionInput.get("intitule")) && questionInput.get("choixUnique").equals(question.isChoixUnique()) && question.getReponsesBonnes().size() == ((List<String>) questionInput.get("reponsesBonnes")).size() && question.getReponsesFausses().size() == ((List<String>) questionInput.get("reponsesFausses")).size() && questionInput.get("time").equals(question.getTime())) {
                            notFound = false;
                            for (int i = 0; i < question.getReponsesBonnes().size(); i++) {
                                if (!question.getReponsesBonnes().get(i).equals(((List<String>) questionInput.get("reponsesBonnes")).get(i))) {
                                    notFound = true;
                                    break;
                                }
                            }
                            for (int i = 0; i < question.getReponsesFausses().size(); i++) {
                                if (!question.getReponsesFausses().get(i).equals(((List<String>) questionInput.get("reponsesFausses")).get(i))) {
                                    notFound = true;
                                    break;
                                }
                            }
                            if (notFound) continue;
                            historique.get().setQuestion(question);
                            break;
                        }
                    }
                    if (notFound)
                        return new Error("updateHistorique", "NOT_FOUND", "Erreur : La question '" + questionInput.get("intitule") + "' n'existe pas.");
                }
                if (dataFetchingEnvironment.containsArgument("date")) {
                    if (StringUtils.normalizeSpace(dataFetchingEnvironment.getArgument("date")).length() <= 0)
                        return new Error("updateHistorique", "INVALID_ARG", "Erreur : La date de l'historique que vous avez saisi : '" + dataFetchingEnvironment.getArgument("date") + "' n'est pas correcte.");
                    historique.get().setDate(dataFetchingEnvironment.getArgument("date"));
                }

                // Sauvegarde dans la base de données
                historiqueRepository.save(historique.get());
                return historique;
            }
            return new Error("removeEnseignant", "TOKEN",
                    "Error : TOKEN");
        };
    }

    public DataFetcher<Object> removeHistorique() {
        return dataFetchingEnvironment -> {
            // On vérifie que l'historique existe
            Optional<Historique> historique = historiqueRepository.findById(Integer.parseInt(dataFetchingEnvironment.getArgument("id_hist")));
            if (!historique.isPresent())
                return new Error("removeHistorique", "NOT_FOUND", "Erreur : Aucun historique correspondant à l'ID : '" + Integer.parseInt(dataFetchingEnvironment.getArgument("id_hist")) + "' n'a été trouvé.");

            // Suppression de l'historique de la base de données
            if (parseJWT(dataFetchingEnvironment.getArgument("token"))) {
                historiqueRepository.delete(historique.get());
                return historique;
            }
            return new Error("removeEnseignant", "TOKEN",
                    "Error : TOKEN");
        };
    }

    private boolean parseJWT(String jwt) {

        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(apiKey))
                .parseClaimsJws(jwt).getBody();
        Date datenow = new Date(System.currentTimeMillis());
        if (claims.getExpiration().after(datenow)) {
            if(claims.getIssuer().equals("api")) return true;
        }
        return false;
    }
}
