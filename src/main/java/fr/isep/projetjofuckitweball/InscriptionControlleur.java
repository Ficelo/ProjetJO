package fr.isep.projetjofuckitweball;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.ByteArrayOutputStream;



public class InscriptionControlleur {
    @FXML    private ImageView bannerView;
    @FXML
    private ImageView imageView;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField ageField;
    @FXML private TextField roleField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField verifField;

    @FXML private Button inscri;
    @FXML private Hyperlink linkRetourConnexion;
    public Label RetourButton;



    public void initialize() {/*
        Image bannerImage = new Image(getClass().getResource("/Images/JoBan.jpg").toExternalForm());
        bannerView.setImage(bannerImage);*/
        }

    @FXML
    private void handleChooseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Images (*.png, *.jpg, *.jpeg, *.gif)", "*.png", "*.jpg", "*.jpeg", "*.gif");
        fileChooser.getExtensionFilters().add(extFilter);

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            imageView.setImage(image);
        }
    }
    @FXML
    private void handleInscription(ActionEvent event) {
        String sexe = genderComboBox.getValue();
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String age = ageField.getText();
        String role = roleField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = verifField.getText();

        if (!password.equals(confirmPassword)) {

            Alert alert = new Alert(Alert.AlertType.ERROR, "Les mots de passe ne correspondent pas.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        Image image = imageView.getImage();

        inscrireUtilisateur(sexe, nom, prenom, age, role, email, password, image);

        redirectionConnexion();
    }

    Image defaultImage = new Image(getClass().getResource("/Images/png-transparent-default-avatar-thumbnail.png").toExternalForm());

    private void inscrireUtilisateur(String sexe, String nom, String prenom, String age, String role, String email, String password, Image image) {
        DB db = new DB();
        Connection connection = db.getConnection();
        if (connection != null) {
            try {
                String query = "INSERT INTO user (sexe, nom, prenom, age, role, email, password, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, sexe);
                preparedStatement.setString(2, nom);
                preparedStatement.setString(3, prenom);
                preparedStatement.setString(4, age);
                preparedStatement.setString(5, role);
                preparedStatement.setString(6, email);
                preparedStatement.setString(7, password);

                if (image != null) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    Files.copy(new File(image.getUrl().substring(5)).toPath(), byteArrayOutputStream);
                    preparedStatement.setBytes(8, byteArrayOutputStream.toByteArray());
                } else {
                    ByteArrayOutputStream defaultImageStream = new ByteArrayOutputStream();
                    Files.copy(new File(defaultImage.getUrl().substring(5)).toPath(), defaultImageStream);
                    preparedStatement.setBytes(8, defaultImageStream.toByteArray());
                }

                preparedStatement.executeUpdate();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void redirectionConnexion()
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Connexion.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) inscri.getScene().getWindow();
            stage.close();

            Stage inscriptionStage = new Stage();
            inscriptionStage.setScene(scene);
            inscriptionStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void retourConnexion() {
        redirectionConnexion();
    }
    public void loadRetour(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Connexion.fxml"));
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

