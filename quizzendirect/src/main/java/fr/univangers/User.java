package fr.univangers;

// OBJET TEMPORAIRE (utilisé pour les tests)
public class User {
    private int id;
    private String prenom;
    private String name;
    private String email;
    private String passwd;

    public int getId() {
        return id;
    }
    public void setId(int id) { this.id= id; }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
