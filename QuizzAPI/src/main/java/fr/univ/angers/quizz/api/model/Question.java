package fr.univ.angers.quizz.api.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.*;

@Data
@Entity
@Table(name = "QUESTION")
public class Question implements Serializable {

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
    @OneToOne()
    @JoinColumn(name = "id_quest")
    private Repertoire repertoire;
    @ElementCollection
    private List<String> reponses = new ArrayList<>();
    @ElementCollection
    private List<Integer> nbReponse = new ArrayList<>();

    public Question() {
    }


    public Question(String intitule, boolean choixUnique, List<String> reponsesBonnes, List<String> reponsesFausses, int time) {
        this.intitule = intitule;
        this.choixUnique = choixUnique;
        this.reponsesBonnes = reponsesBonnes;
        this.reponsesFausses = reponsesFausses;
        this.time = time;
        List<Integer> intergerLists = new ArrayList();
    }

    public int getId_quest() {
        return id_quest;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setChoixUnique(boolean choixUnique) {
        this.choixUnique = choixUnique;
    }

    public boolean isChoixUnique() {
        return choixUnique;
    }

    public void setReponsesBonnes(List<String> reponsesBonnes) {
        this.reponsesBonnes = reponsesBonnes;
    }

    public List<String> getReponsesBonnes() {
        return reponsesBonnes;
    }

    public void addReponseBonne(String reponseBonne) {
        this.reponsesBonnes.add(reponseBonne);
    }

    public void removeReponseBonne(String reponseBonne) {
        this.reponsesBonnes.remove(reponseBonne);
    }

    public void setReponsesFausses(List<String> reponsesFausses) {
        this.reponsesFausses = reponsesFausses;
    }

    public List<String> getReponsesFausses() {
        return reponsesFausses;
    }

    public void addReponseFausse(String reponseFausse) {
        this.reponsesFausses.add(reponseFausse);
    }

    public void removeReponseFausse(String reponseFausse) {
        this.reponsesFausses.remove(reponseFausse);
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public void setRepertoire(Repertoire repertoire) {
        this.repertoire = repertoire;
    }

    public Repertoire getRepertoire() {
        return repertoire;
    }

    public List<String> getReponses() {
        return reponses;
    }

    public void setReponses(List<String> reponses) {
        this.reponses = reponses;
        if (nbReponse.size() == 0)
            for (int i = 0; i < reponses.size(); i++) {
                nbReponse.add(0);
            }
        else{
            for (int i = 0; i < reponses.size(); i++) {
                nbReponse.set(i, 0);
            }
        }
    }

    public List<Integer> getNbReponse() {
        return nbReponse;
    }

    public void setNbReponse(List<Integer> nbreponse) {
        this.nbReponse = nbreponse;
    }

}
