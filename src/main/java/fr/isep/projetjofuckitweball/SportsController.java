package fr.isep.projetjofuckitweball;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class SportsController {

    private Connection connection;
    public Label AcceuilButton;
    public Label SportButton;
    public Label PaysButton;
    public Label AdministrationButton;
    public Label RetourButton;
    public TextField barreRecherche;
    private String retourDestination = "";

    public void initialize() {

        initializeDB();

        barreRecherche.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String query = "SELECT nom FROM discipline";
                try {
                    PreparedStatement statement = connection.prepareStatement(query);
                    ResultSet resultSet = statement.executeQuery();


                    while (resultSet.next()) {
                        String nom = resultSet.getString("nom");
                        if(nom.equals(barreRecherche.getText())) {
                            try {
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Sport.fxml"));
                                Parent root = fxmlLoader.load();

                                Scene scene = new Scene(root);

                                Stage stage = (Stage) RetourButton.getScene().getWindow();

                                stage.setScene(scene);
                                stage.show();

                                SportController controller = fxmlLoader.getController();
                                controller.updateSportTexte(barreRecherche.getText());

                                String fxmlPath = "Sports.fxml";
                                String fxId = barreRecherche.getText();
                                Node node = lookupNode(fxmlPath, fxId);
                                if (node != null) {
                                    if (node instanceof ImageView) {
                                        ImageView imageView = (ImageView) node;
                                        controller.updateSportImage(imageView.getImage());
                                    }
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
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

    public void loadConnexion(MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Connexion.fxml"));
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) AdministrationButton.getScene().getWindow();

            HelloController controlleur = fxmlLoader.getController();
            controlleur.updateRetour("Sports");

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

            SportsController controlleur = fxmlLoader.getController();
            controlleur.updateRetour("Sports");

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

            PaysController controlleur = fxmlLoader.getController();
            controlleur.updateRetour("Sports");

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

            PrincipaleControlleur controlleur = fxmlLoader.getController();
            controlleur.updateRetour("Sports");

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadRetour(MouseEvent mouseEvent) {
        loadAcceuil(mouseEvent);
    }

    public void goToSport(MouseEvent mouseEvent) {
        // Get the source of the event
        Object source = mouseEvent.getSource();

        // Check if the source is an ImageView
        if (source instanceof ImageView) {
            // Cast the source to an ImageView
            ImageView clickedImageView = (ImageView) source;

            // Get the ID of the ImageView
            String imageViewId = clickedImageView.getId();

            // Print the ID of the ImageView
            System.out.println("ImageView ID: " + imageViewId);

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Sport.fxml"));
                Parent root = fxmlLoader.load();

                Scene scene = new Scene(root);

                Stage stage = (Stage) RetourButton.getScene().getWindow();

                stage.setScene(scene);
                stage.show();

                SportController controller = fxmlLoader.getController();
                controller.updateSportTexte(imageViewId);
                controller.updateSportImage(clickedImageView.getImage());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }





    }

    public void updateRetour(String retourDest){
        this.retourDestination = retourDest;
    }

    public static Node lookupNode(String fxmlPath, String fxId) {
        try {
            FXMLLoader loader = new FXMLLoader(SportsController.class.getResource(fxmlPath));
            loader.load();
            return (Node) loader.getNamespace().get(fxId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
