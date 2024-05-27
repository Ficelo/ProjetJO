package fr.isep.projetjofuckitweball;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class PaysIndividuelController {

    public VBox athleteContainer;
    private Connection connection;
    public Label AcceuilButton;
    public Label SportButton;
    public Label PaysButton;
    public Label AdministrationButton;
    public Label RetourButton;
    @FXML public Text nomPays;
    private String retourDestination;

    public void initialize() {


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
            controlleur.updateRetour("Pays");

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
            controlleur.updateRetour("Pays");

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
            controlleur.updateRetour("Pays");

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
            controlleur.updateRetour("Pays");

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadRetour(MouseEvent mouseEvent) {
        loadAcceuil(mouseEvent);
    }

    public void updateRetour(String retourDest){
        this.retourDestination = retourDest;
    }

    public void updateNomPays(String text) {
        this.nomPays.setText(text);
        initializeDB();

        String pays = text;

        String query = "SELECT a.nom, a.prenom, a.age, a.sport, a.event, n.country, a.sexe " +
                "FROM athletes a " +
                "INNER JOIN Nationalities n ON a.nationalite = n.nationality " +
                "WHERE n.country = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, pays);
            System.out.println(statement);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // Retrieve athlete information and print
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String age = resultSet.getString("age");
                String sport = resultSet.getString("sport");
                String event = resultSet.getString("event");
                String country = resultSet.getString("country");
                String sexe = resultSet.getString("sexe");

                HBox athleteBox = new HBox();
                Text nomPrenomText = new Text(nom + " " + prenom);
                nomPrenomText.setFont(new Font(20.0));
                Text sportText = new Text(sport);
                sportText.setFont(new Font(20.0));
                Circle goldCircle = new Circle(15.0, Color.GOLD);
                Circle silverCircle = new Circle(15.0, Color.SILVER);
                Circle bronzeCircle = new Circle(15.0, Color.rgb(205, 127, 50));
                Text goldCountText = new Text("0");
                goldCountText.setFont(new Font(20.0));
                Text silverCountText = new Text("0");
                silverCountText.setFont(new Font(20.0));
                Text bronzeCountText = new Text("0");
                bronzeCountText.setFont(new Font(20.0));

                Insets margin = new Insets(10.0);

                HBox.setMargin(nomPrenomText, margin);
                HBox.setMargin(sportText, margin);

                Insets circleMargin = new Insets(10, 10, 0, 10); // Adjust the margin values as needed

                HBox.setMargin(goldCircle, circleMargin);
                HBox.setMargin(silverCircle, circleMargin);
                HBox.setMargin(bronzeCircle, circleMargin);

                HBox leftHBox = new HBox();
                leftHBox.getChildren().addAll(nomPrenomText, sportText);



                HBox rightHBox = new HBox();
                rightHBox.getChildren().addAll(goldCircle, goldCountText, silverCircle, silverCountText, bronzeCircle, bronzeCountText);
                rightHBox.setAlignment(Pos.CENTER); // Set vertical alignment to center
                rightHBox.setSpacing(10);

                HBox.setHgrow(leftHBox, Priority.ALWAYS); // Add this line


                athleteBox.getChildren().addAll(leftHBox, rightHBox);
                athleteContainer.getChildren().add(athleteBox);

                System.out.println("Nom: " + nom + ", Prenom: " + prenom + ", Age: " + age + ", Sport: " + sport + ", Event: " + event + ", Country: " + country + ", Sexe: " + sexe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
