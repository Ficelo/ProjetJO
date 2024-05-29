package fr.isep.projetjofuckitweball;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AthletesControlleur {
    public Label RetourButton;


    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField ageField;

    @FXML
    private TextField sexeField;

    @FXML
    private TextField sportField;

    @FXML
    private TextField eventField;

    @FXML
    private TextField nationaliteField;
    @FXML private Button connex;

    public void ajouterAthlete(ActionEvent mouseEvent){

        String nom = nomField.getText();
        String prenom = prenomField.getText();

        String age = ageField.getText();
        String sexe = sexeField.getText();
        String sport = sportField.getText();
        String event = eventField.getText();
        String nationalite = nationaliteField.getText();

        System.out.println("Valeurs des variables : ");
        System.out.println("Nom : " + nom);
        System.out.println("Prenom : " + prenom);
        System.out.println("Age : " + age);
        System.out.println("Sport : " + sport);
        System.out.println("Event : " + event);
        System.out.println("Nationalite : " + nationalite);
        System.out.println("Sexe : " + sexe);

        // Connexion à la base de données et insertion des données

        DB db = new DB();
        Connection connection = db.getConnection();

        if (connection != null) {
            try {
                String query = "INSERT INTO athletes (nom, prenom, age, sport, event, nationalite,sexe) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, "'" + nom + "'");
                preparedStatement.setString(2, "'" + prenom + "'");
                preparedStatement.setString(3, "'" + age + "'");
                preparedStatement.setString(4, "'" + sport + "'");
                preparedStatement.setString(5, "'" + event + "'");
                preparedStatement.setString(6, "'" + nationalite + "'");
                preparedStatement.setString(7, "'" + sexe + "'");


                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        redirectionadmin();
    }




    public void loadRetour(MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Administration.fxml"));
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) nomField.getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void redirectionadmin()
    {
        try {
            // Charger le fichier FXML de la page d'inscription
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Administration.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec la racine chargée depuis le fichier FXML
            Scene scene = new Scene(root);

            // Fermer la fenêtre actuelle de connexion
            Stage stage = (Stage) connex.getScene().getWindow();
            stage.close();

            // Créer une nouvelle fenêtre pour la page d'inscription et afficher la scène
            Stage inscriptionStage = new Stage();
            inscriptionStage.setScene(scene);
            inscriptionStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
