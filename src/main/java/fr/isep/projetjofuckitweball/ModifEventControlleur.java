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
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class ModifEventControlleur {

    public Label RetourButton;
    private EventScrapping selectedEvent;
    private Connection connection;

    @FXML
    private TextField eventNameField;
    @FXML
    private TextField disciplineNameField;
    public Button connex;

    public void setEvent(EventScrapping event) {
        this.selectedEvent = event;
        eventNameField.setText(event.getNom());
        disciplineNameField.setText(event.getDiscipline_nom());
    }

    @FXML
    private void modifierEvent(ActionEvent event) {
        String nouveauNom = eventNameField.getText().trim();
        String nouvelleDiscipline = disciplineNameField.getText().trim();


        if (nouveauNom.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez saisir un nom pour l'événement.");
            return;
        }

        try {
            connection = DB.getConnection();

            String updateEventQuery = "UPDATE evenement SET nom = ? WHERE nom = ?";
            PreparedStatement eventStatement = connection.prepareStatement(updateEventQuery);
            eventStatement.setString(1, nouveauNom);
            eventStatement.setString(2, selectedEvent.getNom());

            int eventRowsUpdated = eventStatement.executeUpdate();

            String updateDisciplineQuery = "UPDATE discipline SET nom = ? WHERE nom = ?";
            PreparedStatement disciplineStatement = connection.prepareStatement(updateDisciplineQuery);
            disciplineStatement.setString(1, nouvelleDiscipline);
            disciplineStatement.setString(2, selectedEvent.getDiscipline_nom());
            int disciplineRowsUpdated = disciplineStatement.executeUpdate();

            eventStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la modification de l'événement.");
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


    public void loadRetour(javafx.scene.input.MouseEvent mouseEvent) {
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

