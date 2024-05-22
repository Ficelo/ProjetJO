package fr.isep.projetjofuckitweball;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;

public class HelloController {
    public AnchorPane topBar;
    @FXML    private ImageView bannerView;

@FXML
    private Connection connection;
@FXML
private TextField emailField;
@FXML
private PasswordField passwordField;
@FXML
    private Hyperlink signUpLink;
@FXML
private Button connex;



    public void initialize() {
        Image bannerImage = new Image(getClass().getResource("/Images/JoBan.jpg").toExternalForm());
        bannerView.setImage(bannerImage);

        initializeDB();
        signUpLink.setOnAction(event -> {
            inscription();
        });

        
    }

    private void initializeDB() {
        String databaseName = "app_java";
        String databaseUser = "root";
        String databasePassword = "";
        String url = "jdbc:mysql://localhost/" + databaseName;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, databaseUser, databasePassword);
            System.out.println("Connexion à la base de données établie.");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void loginUser() {
        String email = emailField.getText();
        String password = passwordField.getText();

        // Vérifier si les champs sont vides
        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez saisir votre e-mail et votre mot de passe.");
            return;
        }

        // Vérifier si les informations de connexion sont correctes
        String query = "SELECT * FROM user WHERE email = ? AND password = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Les informations de connexion sont correctes
                showAlert(Alert.AlertType.INFORMATION, "Connexion Réussie", "Vous êtes connecté.");
                redirectionConnexion();
            } else {
                // Les informations de connexion sont incorrectes
                showAlert(Alert.AlertType.ERROR, "Erreur", "L'e-mail ou le mot de passe est incorrect.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la vérification des informations de connexion.");
            e.printStackTrace();
        }
    }
    @FXML
    private void inscription()
    {
        try {
            // Charger le fichier FXML de la page d'inscription
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Inscription.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec la racine chargée depuis le fichier FXML
            Scene scene = new Scene(root);

            // Fermer la fenêtre actuelle de connexion
            Stage stage = (Stage) signUpLink.getScene().getWindow();
            stage.close();

            // Créer une nouvelle fenêtre pour la page d'inscription et afficher la scène
            Stage inscriptionStage = new Stage();
            inscriptionStage.setScene(scene);
            inscriptionStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void redirectionConnexion()
    {
        try {
            // Charger le fichier FXML de la page d'inscription
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Principale.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) connex.getScene().getWindow();
            stage.close();

            Stage PrincipaleStage = new Stage();
            PrincipaleStage.setScene(scene);
            PrincipaleStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
