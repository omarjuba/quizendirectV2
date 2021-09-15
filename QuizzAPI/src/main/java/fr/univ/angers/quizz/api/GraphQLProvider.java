package fr.univ.angers.quizz.api;

import java.io.IOException;
import java.net.URL;

import javax.annotation.PostConstruct;

import fr.univ.angers.quizz.api.datasfetchers.*;
import fr.univ.angers.quizz.api.model.*;
import graphql.TypeResolutionEnvironment;
import graphql.schema.GraphQLObjectType;
import graphql.schema.TypeResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

@Component
public class GraphQLProvider { 

  @Autowired
  private EnseignantDataFetcher enseignantDataFetcher;
  @Autowired
  private QuestionDataFetcher questionDataFetcher;
  @Autowired
  private RepertoireDataFetcher repertoireDataFetcher;
  @Autowired
  private HistoriqueDataFetcher historiqueDataFetcher;
  @Autowired
  private SalonDataFetcher salonDataFetcher;
  @Autowired
  private EtudiantDataFetcher etudiantDataFetcher;
  
  private GraphQL graphQL;

  @PostConstruct
  public void init() throws IOException {
      URL url = Resources.getResource("schema.graphqls");
      String sdl = Resources.toString(url, Charsets.UTF_8);
      GraphQLSchema graphQLSchema = buildSchema(sdl);
      this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
  }

  private GraphQLSchema buildSchema(String sdl) {
      TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
      RuntimeWiring runtimeWiring = buildWiring();
      SchemaGenerator schemaGenerator = new SchemaGenerator();
      return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
  }

  private RuntimeWiring buildWiring() {
      TypeResolver typeResolver = new TypeResolver() {
          @Override
          public GraphQLObjectType getType(TypeResolutionEnvironment env) {
              Object javaObject = env.getObject();
              if (javaObject instanceof Enseignant) {
                  return env.getSchema().getObjectType("Enseignant");
              } else if (javaObject instanceof Question) {
                  return env.getSchema().getObjectType("Question");
              } else if (javaObject instanceof Repertoire) {
                  return env.getSchema().getObjectType("Repertoire");
              } else if (javaObject instanceof Historique) {
                  return env.getSchema().getObjectType("Historique");
              } else if (javaObject instanceof Salon) {
                  return env.getSchema().getObjectType("Salon");
              } else if (javaObject instanceof Etudiant) {
                  return env.getSchema().getObjectType("Etudiant");
              } else {
                  return env.getSchema().getObjectType("Error");
              }
          }
      };
      return RuntimeWiring.newRuntimeWiring()
              .type("Query", typeWiring-> typeWiring

                      .dataFetcher("verifToken", enseignantDataFetcher.checkToken())
                      .dataFetcher("getQuestionByIntitule", questionDataFetcher.getQuestionByIntitule())
                      .dataFetcher("EnseignantVerification", enseignantDataFetcher.getEnseignantVerification())
                      .dataFetcher("allQuestions", questionDataFetcher.getAllQuestion())
                      .dataFetcher("allRepertoires", repertoireDataFetcher.getAllRepertoire())
                      .dataFetcher("allHistoriques", historiqueDataFetcher.getAllHistorique())
                      .dataFetcher("allSalons", salonDataFetcher.getAllSalon())
                      .dataFetcher("allEtudiants", etudiantDataFetcher.getAllEtudiant())
                      .dataFetcher("getEnseignantById", enseignantDataFetcher.getEnseignantById())
                      .dataFetcher("getQuestionById", questionDataFetcher.getQuestionById())
                      .dataFetcher("getRepertoireById", repertoireDataFetcher.getRepertoireById())
                      .dataFetcher("getHistoriqueById", historiqueDataFetcher.getHistoriqueById())
                      .dataFetcher("getSalonById", salonDataFetcher.getSalonById())
                      .dataFetcher("getEtudiantById", etudiantDataFetcher.getEtudiantById())
                      .dataFetcher("getToken", enseignantDataFetcher.getToken())
                      .dataFetcher("EnseignantByMail", enseignantDataFetcher.getEnseignant())
                      .dataFetcher("getMailProf", enseignantDataFetcher.getMailEnseignant())
              )
              .type("Mutation", typeWiring-> typeWiring
                      //    Enseignant
                      .dataFetcher("createEnseignant", enseignantDataFetcher.createEnseignant())
                      .dataFetcher("updateEnseignant", enseignantDataFetcher.updateEnseignant())
                      .dataFetcher("removeEnseignant", enseignantDataFetcher.removeEnseignant())

                      //    Question
                      .dataFetcher("updateReponse", questionDataFetcher.updateReponse())
                      .dataFetcher("createQuestion", questionDataFetcher.createQuestion())
                      .dataFetcher("updateQuestion", questionDataFetcher.updateQuestion())
                      .dataFetcher("removeQuestion", questionDataFetcher.removeQuestion())
                      .dataFetcher("restartQuestionById", questionDataFetcher.restartReponses())

                      //    Repertoire
                      .dataFetcher("createRepertoire", repertoireDataFetcher.createRepertoire())
                      .dataFetcher("updateRepertoire", repertoireDataFetcher.updateRepertoire())
                      .dataFetcher("removeRepertoire", repertoireDataFetcher.removeRepertoire())

                      //    Historique
                      .dataFetcher("createHistorique", historiqueDataFetcher.createHistorique())
                      .dataFetcher("updateHistorique", historiqueDataFetcher.updateHistorique())
                      .dataFetcher("removeHistorique", historiqueDataFetcher.removeHistorique())

                      //    Salon
                      .dataFetcher("createSalon", salonDataFetcher.createSalon())
                      .dataFetcher("updateSalon", salonDataFetcher.updateSalon())
                      .dataFetcher("removeSalon", salonDataFetcher.removeSalon())

                      //    Étudiant
                      .dataFetcher("createEtudiant", etudiantDataFetcher.createEtudiant())
                      .dataFetcher("updateEtudiant", etudiantDataFetcher.updateEtudiant())
                      .dataFetcher("removeEtudiant", etudiantDataFetcher.removeEtudiant())
              )
              .type("EnseignantResult", typeWriting-> typeWriting.typeResolver(typeResolver))
              .type("QuestionResult", typeWriting-> typeWriting.typeResolver(typeResolver))
              .type("RepertoireResult", typeWriting-> typeWriting.typeResolver(typeResolver))
              .type("HistoriqueResult", typeWriting-> typeWriting.typeResolver(typeResolver))
              .type("SalonResult", typeWriting-> typeWriting.typeResolver(typeResolver))
              .type("EtudiantResult", typeWriting-> typeWriting.typeResolver(typeResolver))
              .build();
  }

  @Bean
  public GraphQL graphQL() {
      return graphQL;
  }



}
