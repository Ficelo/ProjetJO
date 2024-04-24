package fr.isep.projetjofuckitweball;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HelloController {
    @FXML
    private ImageView bannerView;

    public void initialize() {
        // Charger l'image de la bannière
        Image bannerImage = new Image("C:\\Users\\arsan\\IdeaProjects\\ProjetJO\\src\\main\\resources\\Images\\JoBan.jpg");
        bannerView.setImage(bannerImage);
    }
}