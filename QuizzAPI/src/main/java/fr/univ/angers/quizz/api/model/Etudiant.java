package fr.univ.angers.quizz.api.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "ETUDIANT")
public class Etudiant implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_etud;
    private String pseudo;
    @OneToOne
    @JoinColumn(name = "id_salon")
    private Salon salon;
    private int bonnesReponses = 0; //Pour les statistiques
    private int questionsRepondues = 0;
    @OneToMany
    private List<Question> questionRepondue;

    public Etudiant(){}
    public Etudiant(String pseudo, Salon salon){
        this.pseudo = pseudo;
        this.salon = salon;
    }

    public int getId_etud() {return this.id_etud;}

    public void setPseudo(String pseudo) {this.pseudo = pseudo;}
    public String getPseudo() {return pseudo;}

    public void setSalon(Salon salon) {
        this.salon = salon;
    }
    public Salon getSalon() {
        return this.salon;
    }

    public void setBonnesReponses(int bonnesReponses) {this.bonnesReponses = bonnesReponses;}
    public int getBonnesReponses() {return bonnesReponses; }

    public void setQuestionsRepondues(int questionsRepondues) { this.questionsRepondues = questionsRepondues; }
    public int getQuestionsRepondues() {return this.questionsRepondues;}
}
