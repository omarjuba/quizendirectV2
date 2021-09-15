package fr.univ.angers.quizz.api.model;

public class Error {

    private String function;
    private String error;
    private String message;

    public Error(String function, String error, String message){
        this.function = function;
        this.error = error;
        this.message = message;
    }

    public void setFunction(String function) {this.function = function;}
    public String getFunction() {return this.function;}

    public void setError(String error) {this.error = error;}
    public String getError() {return this.error;}

    public void setMessage(String message) {this.message = message;}
    public String getMessage() {return this.message;}
}
