package fr.isep.projetjofuckitweball;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class SportController {

    public Label AcceuilButton;
    public Label SportButton;
    public Label PaysButton;
    public Label AdministrationButton;
    public Label RetourButton;
    public ImageView imageSport;
    public Text SportTexte;
    public VBox athleteContainer;
    private String retourDestination = "";
    private Connection connection;

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
        loadSport(mouseEvent);
    }

    public void updateSportTexte(String text) {
        SportTexte.setText(text);

        // C'est vraiment pas fou mais c'est à cause de comment la BDD à été faite suite au scrapping
        // Du coup y'a que certains sports où ça marche

        connection = DB.getConnection();
        athleteContainer.getChildren().clear();

        String query = "SELECT a.nom, a.prenom, a.nationalite, a.event " +
                "FROM athletes a " +
                "JOIN evenement e ON e.nom LIKE CONCAT('%', a.event, '%') " +
                "JOIN discipline d ON e.discipline_id = d.id " +
                "WHERE d.nom = ? ORDER BY a.event";

        HashSet<String> processedAthletes = new HashSet<>();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, text);
            ResultSet resultSet = statement.executeQuery();

            String currentEvent = "";

            while (resultSet.next()) {
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String nationalite = resultSet.getString("nationalite");
                String event = resultSet.getString("event");

                String athleteIdentifier = nom + prenom + nationalite;
                if (processedAthletes.contains(athleteIdentifier)) {
                    continue; // Skip if this athlete has already been processed
                }

                processedAthletes.add(athleteIdentifier);

                if(!event.equals(currentEvent)) {
                    currentEvent = event;
                    Text nomEvent = new Text(event);
                    Text magouille = new Text(" ");
                    Text magouille2 = new Text(" ");
                    nomEvent.setFont(new Font(25.0));
                    magouille.setFont(new Font(25.0));
                    magouille2.setFont(new Font(25.0));

                    // Un peu un technique de clochard mais si ça marche osef
                    athleteContainer.getChildren().add(magouille2);
                    athleteContainer.getChildren().add(nomEvent);
                    athleteContainer.getChildren().add(magouille);
                }

                HBox athleteBox = new HBox();
                Text nomText = new Text(nom);
                Text prenomText = new Text(prenom);
                Text nationaliteText = new Text(nationalite);

                nomText.setFont(new Font(20.0));
                prenomText.setFont(new Font(20.0));
                nationaliteText.setFont(new Font(20.0));


                //athleteBox.getChildren().addAll(nomText, prenomText, nationaliteText, eventText);


                //athleteBox.setSpacing(10);
                HBox leftHBox = new HBox();
                leftHBox.getChildren().addAll(nomText, prenomText);
                leftHBox.setSpacing(10);
                HBox rightHBox = new HBox();
                rightHBox.getChildren().addAll(nationaliteText);
                rightHBox.setAlignment(Pos.CENTER);
                rightHBox.setSpacing(10);

                HBox.setHgrow(leftHBox, Priority.ALWAYS);
                athleteBox.getChildren().addAll(leftHBox, rightHBox);


                athleteContainer.getChildren().add(athleteBox);
            }

            // Close the resultSet and statement
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateSportImage(Image image){
        imageSport.setImage(image);
    }

    public void updateRetour(String retourDest){
        this.retourDestination = retourDest;
    }


}
