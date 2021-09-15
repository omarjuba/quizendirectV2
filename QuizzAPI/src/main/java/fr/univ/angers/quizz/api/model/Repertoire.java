package fr.univ.angers.quizz.api.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "REPERTOIRE")
public class Repertoire implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_rep;
    private String nom;
    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "id_rep")
    private List<Question> questions;
    @OneToOne
    @JoinColumn(name = "id_ens")
    private Enseignant enseignant;

    public Repertoire() {}
    public Repertoire(String nom, Enseignant enseignant){
        this.enseignant = enseignant;
        this.nom = nom;
    }

    public int getId_rep() {return id_rep;}

    public void setNom(String nom) {this.nom = nom;}
    public String getNom() {return nom;}

    public void setQuestions(List<Question> questions) {this.questions = questions;}
    public List<Question> getQuestions() {return questions;}
    public void addQuestion(Question question) {this.questions.add(question);}
    public void removeQuestion(Question question) {this.questions.remove(question);}
    public void removeQuestion(List<Question> questions) {
        for(Question question : questions) removeQuestion(question);
    }

    public void setEnseignant(Enseignant enseignant) {
        this.enseignant = enseignant;
    }
    public Enseignant getEnseignant() {
        return enseignant;
    }
}
