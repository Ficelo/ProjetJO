package fr.isep.projetjofuckitweball;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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


    public void initialize() {
        Image bannerImage = new Image("C:\\Users\\arsan\\IdeaProjects\\ProjetJO\\src\\main\\resources\\Images\\JoBan.jpg");
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
    }
}