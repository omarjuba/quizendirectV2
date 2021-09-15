package fr.univ.angers.quizz.api.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "HISTORIQUE")
public class Historique {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_hist;
    @OneToOne
    @JoinColumn(name = "question")
    private Question question;
    private String date; //Format : "jj/mm/aaaa-hh/mm"

    public Historique() {}
    public Historique(Question question, String date) {
        this.question = question;
        this.date = date;
    }

    public int getId_hist() {return this.id_hist;}

    public void setQuestion(Question question) {this.question = question;}
    public Question getQuestion() {return this.question;}

    public void setDate(String date) {this.date = date;}
    public String getDate() {return date;}
}
