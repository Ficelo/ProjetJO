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

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DiscControlleur {
    public Label RetourButton;
    @FXML
    private TextField discField;
    @FXML private Button connex;

    public void ajtDiscipline(ActionEvent event) {
        String disciplineName = discField.getText();

        DB db = new DB();
        Connection connection = db.getConnection();

        if (connection == null) {
            System.out.println("La connexion à la base de données a échoué.");
            return;
        }

        String query = "INSERT INTO discipline (nom) VALUES (?)";

        try {
            connection.setAutoCommit(true); // Activer l'auto-commit
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, disciplineName);
            System.out.println("Exécution de la requête : " + preparedStatement);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Discipline ajoutée avec succès !");
            } else {
                System.out.println("L'insertion a échoué.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        redirectionadmin();
    }


    public void loadRetour(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Administration.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) RetourButton.getScene().getWindow();
            stage.close();

            Stage inscriptionStage = new Stage();
            inscriptionStage.setScene(scene);
            inscriptionStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void redirectionadmin()
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Administration.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) connex.getScene().getWindow();
            stage.close();

            Stage inscriptionStage = new Stage();
            inscriptionStage.setScene(scene);
            inscriptionStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
