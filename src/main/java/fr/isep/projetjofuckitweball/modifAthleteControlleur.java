package fr.isep.projetjofuckitweball;

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
import java.sql.ResultSet;
import java.sql.SQLException;

public class modifAthleteControlleur {
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField ageField;
    @FXML private TextField sexeField;
    @FXML private TextField sportField;
    @FXML private TextField eventField;
    @FXML private TextField nationaliteField;
    public Label RetourButton;


    private AthleteScrapping selectedAthlete;
    private AthleteScrapping athlete;
    private Connection connection;
    private int idAthlete = 1;

    public Button connex;



    public void setAthlete(AthleteScrapping athlete) {

        connection = DB.getConnection();

        this.selectedAthlete = athlete;
        nomField.setText(athlete.getNom());
        prenomField.setText(athlete.getPrenom());
        ageField.setText(athlete.getAge());
        sexeField.setText(athlete.getSexe());
        sportField.setText(athlete.getSport());
        eventField.setText(athlete.getEvent());
        nationaliteField.setText(athlete.getNationalite());

        String requete = "SELECT id FROM athletes WHERE nom = ? AND prenom = ? AND sport = ? AND age = ? AND sexe = ? AND nationalite = ? AND event = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setString(1, athlete.getNom());
            preparedStatement.setString(2, athlete.getPrenom());
            preparedStatement.setString(3, athlete.getSexe());
            preparedStatement.setString(4, athlete.getSport());
            preparedStatement.setString(5, athlete.getAge());
            preparedStatement.setString(6, athlete.getNationalite());
            preparedStatement.setString(7, athlete.getEvent());
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Récupérer l'ID de l'athlète
            if (resultSet.next()) {
                idAthlete = resultSet.getInt("id");
                System.out.println("ID de l'athlète : " + idAthlete);
            } else {
                System.out.println("Aucun athlète trouvé avec ces paramètres.");
            }

            resultSet.close();
            preparedStatement.close();
            connection.close(); // N'oubliez pas de fermer la connexion après utilisation
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @FXML
    private void modifierAthlete() {
        Connection connection = DB.getConnection();

        String query = "UPDATE athletes SET nom = ?, prenom = ?, age = ?, sport = ?, event = ?, nationalite = ?, sexe = ? WHERE id = ?";


        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nomField.getText());
            statement.setString(2, prenomField.getText());
            statement.setString(3, ageField.getText());
            statement.setString(4, sexeField.getText());
            statement.setString(5, sportField.getText());
            statement.setString(6, eventField.getText());
            statement.setString(7, nationaliteField.getText());
            statement.setInt(8, idAthlete);

            System.out.println(statement);
            statement.executeUpdate();
            statement.close();

            ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        redirectionadmin();
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

    public void loadRetour(MouseEvent mouseEvent) {
        try {
            // Charger le fichier FXML de la page d'inscription
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Administration.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec la racine chargée depuis le fichier FXML
            Scene scene = new Scene(root);

            // Fermer la fenêtre actuelle de connexion
            Stage stage = (Stage) RetourButton.getScene().getWindow();
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
