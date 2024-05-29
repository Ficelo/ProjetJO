package fr.isep.projetjofuckitweball;

public class ResultatScrapping {

    String nom;
    String gold;
    String silver;
    String bronze;

    public ResultatScrapping(String nom, String gold, String silver, String bronze) {
        this.nom = nom;
        this.gold = gold;
        this.silver = silver;
        this.bronze = bronze;
    }


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getGold() {
        return gold;
    }

    public void setGold(String gold) {
        this.gold = gold;
    }

    public String getSilver() {
        return silver;
    }

    public void setSilver(String silver) {
        this.silver = silver;
    }

    public String getBronze() {
        return bronze;
    }

    public void setBronze(String bronze) {
        this.bronze = bronze;
    }
}
