package fr.isep.projetjofuckitweball;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    @FXML private Button inscri;



    public void initialize() {
        Image bannerImage = new Image("C:\\Users\\arsan\\IdeaProjects\\ProjetJO\\src\\main\\resources\\Images\\JoBan.jpg");
        bannerView.setImage(bannerImage);
        }

    @FXML
    private void handleChooseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");

        // Filtre pour ne montrer que les fichiers d'images
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Images (*.png, *.jpg, *.jpeg, *.gif)", "*.png", "*.jpg", "*.jpeg", "*.gif");
        fileChooser.getExtensionFilters().add(extFilter);

        // Afficher la boîte de dialogue et attendre que l'utilisateur sélectionne un fichier
        File selectedFile = fileChooser.showOpenDialog(null);

        // Si un fichier est sélectionné, charger l'image dans l'application
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            imageView.setImage(image);
        }
    }
    @FXML
    private void handleInscription(ActionEvent event) {
        // Récupérer les autres informations de l'utilisateur
        String sexe = genderComboBox.getValue();
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String age = ageField.getText();
        String role = roleField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        // Récupérer l'image sélectionnée par l'utilisateur
        Image image = imageView.getImage();

        // Inscrire l'utilisateur dans la base de données
        //inscrireUtilisateur(sexe, nom, prenom, age, role, email, password, image);

        redirectionConnexion();
    }

    private Image defaultImage = new Image("C:\\Users\\arsan\\IdeaProjects\\ProjetJO\\src\\main\\resources\\Images\\png-transparent-default-avatar-thumbnail.png");
    /*
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
                    // Si l'image est nulle, utilisez l'image par défaut
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
    */
    private void redirectionConnexion()
    {
        try {
            // Charger le fichier FXML de la page d'inscription
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec la racine chargée depuis le fichier FXML
            Scene scene = new Scene(root);

            // Fermer la fenêtre actuelle de connexion
            Stage stage = (Stage) inscri.getScene().getWindow();
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

