package fr.isep.projetjofuckitweball;

public class AthleteScrapping {

    String nom;
    String prenom;
    String discipline;
    String age;
    String sexe;
    String role = "Athlete";

    public AthleteScrapping(String nom, String prenom, String discipline, String age, String sexe, String role) {
        this.nom = nom;
        this.prenom = prenom;
        this.discipline = discipline;
        this.age = age;
        this.sexe = sexe;
        this.role = role;
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

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
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
