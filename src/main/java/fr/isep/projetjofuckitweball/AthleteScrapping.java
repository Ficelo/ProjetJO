package fr.isep.projetjofuckitweball;

public class AthleteScrapping {

    String nom;
    String prenom;
    String sport;
    String age;
    String sexe;
    String role = "Athlete";
    String nationalite;
    String event;

    public AthleteScrapping(String nom, String prenom, String sport, String age, String sexe, String nationalite, String event) {
        this.nom = nom;
        this.prenom = prenom;
        this.sport = sport;
        this.age = age;
        this.sexe = sexe;
        this.nationalite = nationalite;
        this.event = event;
    }

    public String getNationalite() {
        return nationalite;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public AthleteScrapping(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
