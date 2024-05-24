package fr.isep.projetjofuckitweball;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PrincipaleControlleur {
    public AnchorPane topBar;
    public Label AthleteButton;
    public Label EvenementButton;
    public Label DisciplinesButton;
    public Label AcceuilButton;
    @FXML
    private ImageView bannerView;
    @FXML
    private Connection connection;

    @FXML
    private ImageView profileImageView;

    @FXML private ListView listView;

    public void initialize() {
        topBar.setStyle("-fx-background-color: #2E1D74;");
        AthleteButton.setStyle("-fx-text-fill: white;");
        EvenementButton.setStyle("-fx-text-fill: white;");
        DisciplinesButton.setStyle("-fx-text-fill: white;");
        AcceuilButton.setStyle("-fx-text-fill: white;");

        AthleteButton.setOnMouseEntered(event -> {
            AthleteButton.setStyle("-fx-background-color: #8672d8; -fx-text-fill: white;");
        });

        AthleteButton.setOnMouseExited(event -> {
            AthleteButton.setStyle("-fx-background-color: #2E1D74; -fx-text-fill: white;");
        });

        EvenementButton.setOnMouseEntered(event -> {
            EvenementButton.setStyle("-fx-background-color: #8672d8; -fx-text-fill: white;");
        });

        EvenementButton.setOnMouseExited(event -> {
            EvenementButton.setStyle("-fx-background-color: #2E1D74; -fx-text-fill: white;");
        });

        DisciplinesButton.setOnMouseEntered(event -> {
            DisciplinesButton.setStyle("-fx-background-color: #8672d8; -fx-text-fill: white;");
        });

        DisciplinesButton.setOnMouseExited(event -> {
            DisciplinesButton.setStyle("-fx-background-color: #2E1D74; -fx-text-fill: white;");
        });

        AcceuilButton.setOnMouseEntered(event -> {
            AcceuilButton.setStyle("-fx-background-color: #8672d8; -fx-text-fill: white;");
        });

        AcceuilButton.setOnMouseExited(event -> {
            AcceuilButton.setStyle("-fx-background-color: #2E1D74; -fx-text-fill: white;");
        });

        Image bannerImage = new Image(getClass().getResource("/Images/JoBan.jpg").toExternalForm());
        bannerView.setImage(bannerImage);

        try {
            DB db = new DB();
            connection = db.getConnection();
            int userId = AuthService.getLoggedInUserId();

            System.out.println("User ID: " + userId); // Vérification de l'ID utilisateur

            if (userId != -1) {
                String sql = "SELECT image FROM user WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, userId);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    byte[] imageData = resultSet.getBytes("image");
                    ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
                    Image image = new Image(bis);
                    profileImageView.setImage(image);
                }

                resultSet.close();
                statement.close();
            } else {
                System.out.println("Aucun utilisateur connecté.");
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadDiscipline(MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Discipline.fxml"));
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) EvenementButton.getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadEvenement(MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Discipline.fxml"));
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) EvenementButton.getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAthlete(MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Athlete.fxml"));
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) AthleteButton.getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAcceuil(MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Principale.fxml"));
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) AcceuilButton.getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
