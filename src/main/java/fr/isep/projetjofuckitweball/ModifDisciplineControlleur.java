package fr.isep.projetjofuckitweball;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ModifDisciplineControlleur {

    public Label RetourButton;
    private PaysScrapping selectedDiscipline;
    private  Connection connection;


    @FXML
    private TextField nomField;
    public Button connex;


    public void setDiscipline(PaysScrapping discipline) {
        this.selectedDiscipline = discipline;
        nomField.setText(discipline.getNom());
    }
    @FXML
    private void modifierDiscipline(ActionEvent event) {
        String nouveauNom = nomField.getText().trim();

        if (nouveauNom.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez saisir un nom pour la discipline.");
            return;
        }

        try {
            connection = DB.getConnection();

            String query = "UPDATE discipline SET nom = ? WHERE nom = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nouveauNom);
            statement.setString(2, selectedDiscipline.getNom());

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Le nom de la discipline a été modifié avec succès.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la modification du nom de la discipline.");
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la modification de la discipline.");
        }
        redirectionadmin();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
}
