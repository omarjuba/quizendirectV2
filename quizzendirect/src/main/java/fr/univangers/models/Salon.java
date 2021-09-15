package fr.univangers.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class Salon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_salon;
    private int code_acces;
    @OneToMany
    private List<Question> questionsEnAttente;
    @OneToMany
    private List<Question> questionsPosees;
    @OneToOne(cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "questioncourante")
    private Question questionCourante;
    @OneToMany
    private List<Etudiant> etudiants;
    @OneToOne(cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "enseignant")
    private Enseignant enseignant;

    public Salon(){}
    public Salon(int code_acces, Enseignant enseignant){
        this.code_acces = code_acces;
        this.enseignant = enseignant;
    }

    public void setId_salon(int id_salon) {this.id_salon = id_salon;}
    public int getId_salon() {return id_salon;}

    public void setCode_acces(int code_acces) {this.code_acces = code_acces;}
    public int getCode_acces() {return code_acces;}

    public void setQuestionsEnAttente(List<Question> questionsEnAttente) {this.questionsEnAttente = questionsEnAttente; }
    public List<Question> getQuestionEnAttente() {return questionsEnAttente;}

    public List<Question> getQuestionPosees() {return this.questionsPosees;}

    public void setQuestionCourante(Question questionCourante) {this.questionCourante = questionCourante;}
    public Question getQuestionCourante() {return this.questionCourante;}

    public void setEtudiants(List<Etudiant> etudiants) {this.etudiants = etudiants;}
    public List<Etudiant> getEtudiants() {return this.etudiants;}

    public void setEnseignant(Enseignant enseignant) {this.enseignant = enseignant;}
    public Enseignant getEnseignant() {return this.enseignant;}
}

