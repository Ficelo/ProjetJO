package fr.isep.projetjofuckitweball;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class PrincipaleControlleur {
    @FXML
    private ImageView bannerView;
    @FXML
    private Connection connection;

    @FXML
    private ImageView profileImageView;

    @FXML private GridPane gridPane;
    @FXML private ListView listView;
    @FXML private Label labelScore;


    public void initialize() {
        Image bannerImage = new Image(getClass().getResource("/Images/JoBan.jpg").toExternalForm());
        bannerView.setImage(bannerImage);


        try {
            DB db = new DB();
            Connection connection = db.getConnection();

            // Récupérer l'ID de l'utilisateur connecté (par exemple, à partir d'une classe AuthService)
            int userId = AuthService.getLoggedInUserId();

            // Requête SQL pour récupérer les données binaires de l'image de l'utilisateur connecté
            String sql = "SELECT image FROM user WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Récupérer les données binaires de l'image :D
                byte[] imageData = resultSet.getBytes("image");

                // Créer une image à partir des données binaires
                ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
                Image image = new Image(bis);

                // Définir l'image sur l'ImageView
                profileImageView.setImage(image);
            }


            // Fermer les ressources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Jpense que je vais pas me faire chier et que je vais remplacer ça par un gros dropdown de bz
        // Ça va me fare perdre trop de temps de faire un calendrier à la mano comme ça

        int jour = 0;
        for(int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                jour++;
                if(jour <= 31){
                    Label label = new Label(jour + "");
                    label.getStyleClass().add("calendar-label");
                    gridPane.add(label, j, i);
                }
            }
        }

        for(int i = 0; i < 15; i++) {
            Label label = new Label("Epreuve de 200m nage libre | date : " + (i+1) + " Aout");
            listView.getItems().add(label);
        }

        // Les emojis ça marche pas sur les labels >:c
        labelScore.setText("USA 🇺🇸 : OR = 5, ARGENT = 4, Bronze = 6");



    }
}