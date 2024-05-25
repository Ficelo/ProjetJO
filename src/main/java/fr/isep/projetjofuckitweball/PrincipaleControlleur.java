package fr.isep.projetjofuckitweball;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class PrincipaleControlleur {
    public Label AcceuilButton;
    public Label SportButton;
    public Label PaysButton;
    public Label AdministrationButton;
    public Label RetourButton;

    /*
    public void initialize() {
        topBar.setStyle("-fx-background-color: #2E1D74;");
        SportButton.setStyle("-fx-text-fill: white;");
        PaysButton.setStyle("-fx-text-fill: white;");
        DisciplinesButton.setStyle("-fx-text-fill: white;");
        AcceuilButton.setStyle("-fx-text-fill: white;");

        SportButton.setOnMouseEntered(event -> {
            SportButton.setStyle("-fx-background-color: #8672d8; -fx-text-fill: white;");
        });

        SportButton.setOnMouseExited(event -> {
            SportButton.setStyle("-fx-background-color: #2E1D74; -fx-text-fill: white;");
        });

        PaysButton.setOnMouseEntered(event -> {
            PaysButton.setStyle("-fx-background-color: #8672d8; -fx-text-fill: white;");
        });

        PaysButton.setOnMouseExited(event -> {
            PaysButton.setStyle("-fx-background-color: #2E1D74; -fx-text-fill: white;");
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
*/
    public void loadConnexion(MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Connexion.fxml"));
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) AdministrationButton.getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadSport(MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Sports.fxml"));
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) SportButton.getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPays(MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Pays.fxml"));
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) PaysButton.getScene().getWindow();

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

            Stage stage = (Stage) RetourButton.getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadRetour(MouseEvent mouseEvent) {
        loadAcceuil(mouseEvent);
    }
}
