package fr.isep.projetjofuckitweball;

public class PaysScrapping {

    private String nom;
    private String url;

    public PaysScrapping(String nom, String url) {
        this.nom = nom;
        this.url = url;
    }

    @Override
    public String toString() {
        return nom + " : \n" + url + "\n";
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
