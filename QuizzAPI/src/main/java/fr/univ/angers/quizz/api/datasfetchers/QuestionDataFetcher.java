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
public class QuestionDataFetcher {

    @Value("${apiKey}")
    private String apiKey;

    @Autowired
    private EnseignantRepository enseignantRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private RepertoireRepository repertoireRepository;
    int idEns;

    public DataFetcher<List<Question>> getAllQuestion() {
        return dataFetchingEnvironment -> questionRepository.findAll();
    }

    public DataFetcher<Object> getQuestionById() {
        return dataFetchingEnvironment -> {
            Optional<Question> question = questionRepository.findById(Integer.parseInt(dataFetchingEnvironment.getArgument("id_quest")));
            if (question.isPresent()) return question.get();
            // Question non trouvée
            return new Error("getQuestionById", "NOT_FOUND", "Erreur : Aucune question correspondant à l'ID : '" + Integer.parseInt(dataFetchingEnvironment.getArgument("id_quest")) + "' n'a été trouvée.");
        };
    }

    public DataFetcher<Object> getQuestionByIntitule() {
        return dataFetchingEnvironment -> {
            Question question = questionRepository.findQuestionByIntitule(dataFetchingEnvironment.getArgument("intitule"));
            if (question != null) return question;
            return new Question();
        };
    }

    public DataFetcher<Object> createQuestion() {
        return dataFetchingEnvironment -> {
            // On vérifie que toutes les données passées en paramètres sont valides
            if (parseJWT(dataFetchingEnvironment.getArgument("token"))) {
                if (StringUtils.normalizeSpace(dataFetchingEnvironment.getArgument("intitule")).length() <= 0)
                    return new Error("createQuestion", "INVALID_ARG", "Erreur : L'intitulé de la question que vous avez saisi : '" + dataFetchingEnvironment.getArgument("intitule") + "' n'est pas correct.");
                if (((List<String>) dataFetchingEnvironment.getArgument("reponsesBonnes")).isEmpty())
                    return new Error("createQuestion", "INVALID_ARG", "Erreur : Vous devez saisir au minimum 1 bonne réponse.");
                if ((((List<String>) dataFetchingEnvironment.getArgument("reponsesBonnes")).size() + ((List<String>) dataFetchingEnvironment.getArgument("reponsesFausses")).size()) < 2)
                    return new Error("createQuestion", "INVALID_ARG", "Erreur : Vous devez saisir au minimum 2 réponses possibles.");
                if ((((List<String>) dataFetchingEnvironment.getArgument("reponsesBonnes")).size() + ((List<String>) dataFetchingEnvironment.getArgument("reponsesFausses")).size()) > 4)
                    return new Error("createQuestion", "INVALID_ARG", "Erreur : Vous devez saisir au maximum 4 réponses possibles.");

                List<String> reponses = new ArrayList<>();
                reponses.addAll(dataFetchingEnvironment.getArgument("reponsesBonnes"));
                reponses.addAll(dataFetchingEnvironment.getArgument("reponsesFausses"));
                // On vérifie que la question n'existe pas déjà
                List<Repertoire> repertoires = enseignantRepository.findById(idEns).get().getRepertoires();
                Repertoire repertoire = null;
                for (Repertoire rep: repertoires) {
                    if(rep.getNom().equals(dataFetchingEnvironment.getArgument("nomRepertoire"))) repertoire = rep;
                }
                for (int i = 0; i < reponses.size() - 1; i++) {
                    for (int j = i + 1; j < reponses.size(); j++) {
                        if (reponses.get(i).equals(reponses.get(j)))
                            return new Error("createQuestion", "INVALID_ARG", "Erreur : Vous avez saisi plusieurs réponses identiques.");
                    }
                }

                if (((int) dataFetchingEnvironment.getArgument("time")) <= 0)
                    return new Error("createQuestion", "INVALID_ARG", "Erreur : Le temps de réponse à la question que vous avez saisi : '" + dataFetchingEnvironment.getArgument("time") + "' secondes, n'est pas correct.");


                List<Question> questions = repertoire.getQuestions();
                for (Question question : questions) {
                    if (dataFetchingEnvironment.getArgument("intitule").equals(question.getIntitule()) && dataFetchingEnvironment.getArgument("choixUnique").equals(question.isChoixUnique()) && dataFetchingEnvironment.getArgument("reponsesBonnes").equals(question.getReponsesBonnes()) && dataFetchingEnvironment.getArgument("reponsesFausses").equals(question.getReponsesFausses()) && dataFetchingEnvironment.getArgument("time").equals(question.getTime()))
                        return new Error("createQuestion", "ALREADY_EXISTS", "Erreur : Cette question existe déjà.");
                }

                // On crée la nouvelle question
                Question nouvelleQuestion = new Question(dataFetchingEnvironment.getArgument("intitule"), dataFetchingEnvironment.getArgument("choixUnique"), dataFetchingEnvironment.getArgument("reponsesBonnes"), dataFetchingEnvironment.getArgument("reponsesFausses"), dataFetchingEnvironment.getArgument("time"));
                nouvelleQuestion.setReponses(reponses);

                questions.add(nouvelleQuestion);
                repertoire.setQuestions(questions);

                // Sauvegarde dans la base de données
                repertoireRepository.save(repertoire);
                return nouvelleQuestion;
            }
            return new Error("removeEnseignant", "TOKEN",
                    "Error : TOKEN");
        };
    }
    public DataFetcher<Object> restartReponses(){
        return dataFetchingEnvironment -> {
            if (!parseJWT(dataFetchingEnvironment.getArgument("token")))
                return new Error("restartReponses", "TOKEN",
                        "Error : TOKEN");
            Optional<Question> question = questionRepository.findById(Integer.parseInt(dataFetchingEnvironment.getArgument("id_quest")));
            if (!question.isPresent())
                return new Error("updateQuestion", "NOT_FOUND", "Erreur : Aucune question correspondant à l'ID : '" + Integer.parseInt(dataFetchingEnvironment.getArgument("id_quest")) + "' n'a été trouvée.");
            question.get().setReponses(question.get().getReponses());
            questionRepository.save(question.get());
            return question.get();
        };
    }

    public DataFetcher<Object> updateReponse() {
        return dataFetchingEnvironment -> {
            Optional<Question> question = questionRepository.findById(Integer.parseInt(dataFetchingEnvironment.getArgument("id_quest")));
            if (!question.isPresent())
                return new Error("updateQuestion", "NOT_FOUND", "Erreur : Aucune question correspondant à l'ID : '" + Integer.parseInt(dataFetchingEnvironment.getArgument("id_quest")) + "' n'a été trouvée.");

            //recuperation reponse etudiant
            String reponse = dataFetchingEnvironment.getArgument("reponse");

            //creer tableautmp
            List<Integer> nbReponsetmp =  question.get().getNbReponse();

            int index = question.get().getReponses().indexOf(reponse);
            nbReponsetmp.set(index , question.get().getNbReponse().get(index)+1);

            question.get().setNbReponse(nbReponsetmp);
            questionRepository.save(question.get());
            Optional<Question> newQ = questionRepository.findById(Integer.parseInt(dataFetchingEnvironment.getArgument("id_quest")));
            return question.get();
        };
    }

    public DataFetcher<Object> updateQuestion() {
        return dataFetchingEnvironment -> {
            // On vérifie que la question existe
            Optional<Question> question = questionRepository.findById(Integer.parseInt(dataFetchingEnvironment.getArgument("id_quest")));
            if (!question.isPresent())
                return new Error("updateQuestion", "NOT_FOUND", "Erreur : Aucune question correspondant à l'ID : '" + Integer.parseInt(dataFetchingEnvironment.getArgument("id_quest")) + "' n'a été trouvée.");

            // On modifie les attributs passés en paramètres si les nouvelles valeurs sont valides
            if (parseJWT(dataFetchingEnvironment.getArgument("token"))) {
                if (dataFetchingEnvironment.containsArgument("intitule")) {
                    if (StringUtils.normalizeSpace(dataFetchingEnvironment.getArgument("intitule")).length() <= 0)
                        return new Error("updateQuestion", "INVALID_ARG", "Erreur : L'intitulé de la question que vous avez saisi : '" + dataFetchingEnvironment.getArgument("intitule") + "' n'est pas correct.");
                    question.get().setIntitule(dataFetchingEnvironment.getArgument("intitule"));
                }
                if (dataFetchingEnvironment.containsArgument("choixUnique"))
                    question.get().setChoixUnique(dataFetchingEnvironment.getArgument("choixUnique"));
                if (dataFetchingEnvironment.containsArgument("reponsesBonnes")) {
                    if (((List<String>) dataFetchingEnvironment.getArgument("reponsesBonnes")).isEmpty())
                        return new Error("updateQuestion", "INVALID_ARG", "Erreur : Vous devez saisir au minimum 1 bonne réponse.");
                    List<String> reponses = new ArrayList<>();
                    reponses.addAll(dataFetchingEnvironment.getArgument("reponsesBonnes"));
                    reponses.addAll(question.get().getReponsesFausses());
                    for (int i = 0; i < reponses.size() - 1; i++) {
                        for (int j = i + 1; j < reponses.size(); j++) {
                            if (reponses.get(i).equals(reponses.get(j)))
                                return new Error("updateQuestion", "INVALID_ARG", "Erreur : Une question ne peut pas avoir plusieurs réponses identiques.");
                        }
                    }
                    question.get().setReponsesBonnes(dataFetchingEnvironment.getArgument("reponsesBonnes"));
                }
                if (dataFetchingEnvironment.containsArgument("reponsesFausses")) {
                    if ((question.get().getReponsesBonnes().size() + ((List<String>) dataFetchingEnvironment.getArgument("reponsesFausses")).size()) < 2)
                        return new Error("updateQuestion", "INVALID_ARG", "Erreur : La question doit contenir au minimum 2 réponses possibles (actuellement : " + question.get().getReponsesBonnes().size() + " bonne(s) et " + question.get().getReponsesFausses().size() + " fausse(s)).");
                    if ((question.get().getReponsesBonnes().size() + ((List<String>) dataFetchingEnvironment.getArgument("reponsesFausses")).size()) > 4)
                        return new Error("updateQuestion", "INVALID_ARG", "Erreur : La question ne peut pas contenir plus de 4 réponses possibles (actuellement : " + question.get().getReponsesBonnes().size() + " bonne(s) et " + question.get().getReponsesFausses().size() + " fausse(s)).");
                    List<String> reponses = new ArrayList<>();
                    reponses.addAll(question.get().getReponsesBonnes());
                    reponses.addAll(dataFetchingEnvironment.getArgument("reponsesFausses"));
                    for (int i = 0; i < reponses.size() - 1; i++) {
                        for (int j = i + 1; j < reponses.size(); j++) {
                            if (reponses.get(i).equals(reponses.get(j)))
                                return new Error("updateQuestion", "INVALID_ARG", "Erreur : Une question ne peut pas avoir plusieurs réponses identiques.");
                        }
                    }
                    question.get().setReponsesFausses(dataFetchingEnvironment.getArgument("reponsesFausses"));
                }
                if (dataFetchingEnvironment.containsArgument("time")) {
                    if (((int) dataFetchingEnvironment.getArgument("time")) <= 0)
                        return new Error("updateQuestion", "INVALID_ARG", "Erreur : Le temps de réponse à la question que vous avez saisi : '" + dataFetchingEnvironment.getArgument("time") + "' secondes, n'est pas correct.");
                    question.get().setTime(dataFetchingEnvironment.getArgument("time"));
                }

                if (dataFetchingEnvironment.containsArgument("repertoire")) {
                    // On ajoute les nouveaux répertoires
                    Map<String, Object> repertoireInput = dataFetchingEnvironment.getArgument("repertoire");
                    List<Repertoire> repertoires = repertoireRepository.findAll();
                    Map<String, Object> enseignantInput = (Map<String, Object>) repertoireInput.get("enseignant");
                    boolean repertoireExists = false;
                    for (Repertoire repertoire : repertoires) {
                        if (repertoire.getNom().equals(repertoireInput.get("nom")) && repertoire.getEnseignant().getMail().equals(enseignantInput.get("mail"))) {
                            question.get().setRepertoire(repertoire);
                            repertoireExists = true;
                            break;
                        }
                    }
                    if (!repertoireExists)
                        return new Error("updateQuestion", "NOT_FOUND", "Erreur : Le répertoire '" + repertoireInput.get("nom") + "' de l'enseignant : '" + enseignantInput.get("mail") + "' n'existe pas.");
                }

                // Sauvegarde dans la base de données
                questionRepository.save(question.get());
                return question;
            }
            return new Error("removeEnseignant", "TOKEN",
                    "Error : TOKEN");
        };
    }

    public DataFetcher<Object> removeQuestion() {
        return dataFetchingEnvironment -> {
            // On vérifie que la question existe
            Optional<Question> question = questionRepository.findById(Integer.parseInt(dataFetchingEnvironment.getArgument("id_quest")));
            if (!question.isPresent())
                return new Error("removeQuestion", "NOT_FOUND", "Erreur : Aucune question correspondant à l'ID : '" + Integer.parseInt(dataFetchingEnvironment.getArgument("id_quest")) + "' n'a été trouvée.");

            // Suppression de la question de la base de données
            questionRepository.delete(question.get());
            return question;
        };
    }

    private boolean parseJWT(String jwt) {

        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(apiKey))
                .parseClaimsJws(jwt).getBody();
        idEns = Integer.parseInt(claims.getId());
        Date datenow = new Date(System.currentTimeMillis());
        if (claims.getExpiration().after(datenow)) {
            if (claims.getIssuer().equals("api")) return true;
        }
        return false;
    }
}
