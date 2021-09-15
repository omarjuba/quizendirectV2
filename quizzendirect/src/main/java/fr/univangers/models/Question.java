package fr.univangers.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_quest;
    private String intitule;
    private boolean choixUnique;
    @ElementCollection
    private List<String> reponsesBonnes;
    @ElementCollection
    private List<String> reponsesFausses;
    private int time; //En secondes
    @OneToOne(cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "repertoire")
    private Repertoire repertoire;

    public Question() {}
    public Question(String intitule, boolean choixUnique, List<String> reponsesBonnes, List<String> reponsesFausses, int time){
        this.intitule = intitule;
        this.choixUnique = choixUnique;
        this.reponsesBonnes = reponsesBonnes;
        this.reponsesFausses = reponsesFausses;
        this.time = time;
    }

    public void setId_quest(int id_quest) {this.id_quest = id_quest;}
    public int getId_quest() {return id_quest;}

    public void setIntitule(String intitule) {this.intitule = intitule;}
    public String getIntitule() {return intitule;}

    public void setChoixUnique(boolean choixUnique) {this.choixUnique = choixUnique;}
    public boolean isChoixUnique() {return choixUnique;}

    public void setReponsesBonnes(List<String> reponsesBonnes) {this.reponsesBonnes = reponsesBonnes;}
    public List<String> getReponsesBonnes() {return reponsesBonnes;}

    public void setReponsesFausses(List<String> reponsesFausses) {this.reponsesFausses = reponsesFausses;}
    public List<String> getReponsesFausses() {return reponsesFausses;}

    public void setTime(int time) {this.time = time;}
    public int getTime() {return time;}

    public void setRepertoire(Repertoire repertoire) {
        this.repertoire = repertoire;
    }
    public Repertoire getRepertoire() {
        return repertoire;
    }
}