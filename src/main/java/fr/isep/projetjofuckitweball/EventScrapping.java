package fr.isep.projetjofuckitweball;

public class EventScrapping {

    String nom;
    String discipline_nom;

    public EventScrapping(String nom, String discipline_nom) {
        this.nom = nom;
        this.discipline_nom = discipline_nom;
    }

    public String getDiscipline_nom() {
        return discipline_nom;
    }

    public void setDiscipline_nom(String discipline_nom) {
        this.discipline_nom = discipline_nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
