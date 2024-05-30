package fr.isep.projetjofuckitweball;

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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;

public class PaysIndividuelController {

    @FXML private VBox athleteContainer;
    @FXML private Text texteGold;
    @FXML private Text texteSilver;
    @FXML private Text texteBronze;
    @FXML private Label AcceuilButton;
    @FXML private Label SportButton;
    @FXML private Label PaysButton;
    @FXML private Label AdministrationButton;
    @FXML private Label RetourButton;
    @FXML private Text nomPays;
    @FXML private Label RapportButton;
    private Connection connection;
    private String retourDestination;
    private int totalBronze = 0;
    private int totalSilver = 0;
    private int totalGold = 0;

    public void initialize() {
        connection = DB.getConnection();
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
        loadPays(mouseEvent);
    }

    public void updateRetour(String retourDest) {
        this.retourDestination = retourDest;
    }

    public void updateNomPays(String text) throws SQLException {
        this.nomPays.setText(text);
        String pays = text;
        String query = "SELECT a.nom, a.prenom, a.age, a.sport, a.event, n.country, a.sexe " +
                "FROM athletes a " +
                "INNER JOIN Nationalities n ON a.nationalite = n.nationality " +
                "WHERE n.country = ?";
        HashMap<String, Text[]> athletes = new HashMap<>();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, pays);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
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

                athletes.put(nom, new Text[] {goldCountText, silverCountText, bronzeCountText});

                Insets margin = new Insets(10.0);
                HBox.setMargin(nomPrenomText, margin);
                HBox.setMargin(sportText, margin);

                Insets circleMargin = new Insets(10, 10, 0, 10);
                HBox.setMargin(goldCircle, circleMargin);
                HBox.setMargin(silverCircle, circleMargin);
                HBox.setMargin(bronzeCircle, circleMargin);

                HBox leftHBox = new HBox();
                leftHBox.getChildren().addAll(nomPrenomText, sportText);
                HBox rightHBox = new HBox();
                rightHBox.getChildren().addAll(goldCircle, goldCountText, silverCircle, silverCountText, bronzeCircle, bronzeCountText);
                rightHBox.setAlignment(Pos.CENTER);
                rightHBox.setSpacing(10);

                HBox.setHgrow(leftHBox, Priority.ALWAYS);
                athleteBox.getChildren().addAll(leftHBox, rightHBox);
                athleteContainer.getChildren().add(athleteBox);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query2 = "SELECT a.id, " +
                "COUNT(CASE WHEN r.bronze = a.id THEN 1 END) AS bronze_medals, " +
                "COUNT(CASE WHEN r.silver = a.id THEN 1 END) AS silver_medals, " +
                "COUNT(CASE WHEN r.gold = a.id THEN 1 END) AS gold_medals " +
                "FROM athletes a " +
                "LEFT JOIN resultats r ON a.id = r.bronze OR a.id = r.silver OR a.id = r.gold " +
                "WHERE a.nom = ?";
        for (String nom : athletes.keySet()) {
            PreparedStatement statement2 = connection.prepareStatement(query2);
            statement2.setString(1, nom);
            ResultSet resultSet2 = statement2.executeQuery();
            while (resultSet2.next()) {
                int id = resultSet2.getInt("id");
                int bronzeMedals = resultSet2.getInt("bronze_medals");
                int silverMedals = resultSet2.getInt("silver_medals");
                int goldMedals = resultSet2.getInt("gold_medals");
                Text[] counts = athletes.get(nom);
                counts[0].setText(String.valueOf(goldMedals));
                counts[1].setText(String.valueOf(silverMedals));
                counts[2].setText(String.valueOf(bronzeMedals));

                totalBronze += bronzeMedals;
                totalSilver += silverMedals;
                totalGold += goldMedals;
            }
            resultSet2.close();
            statement2.close();
        }

        texteGold.setText(Integer.toString(totalGold));
        texteSilver.setText(Integer.toString(totalSilver));
        texteBronze.setText(Integer.toString(totalBronze));

        String updatePaysQuery = "UPDATE pays SET gold = ?, silver = ?, bronze = ? WHERE nom = ?";
        PreparedStatement statement3 = connection.prepareStatement(updatePaysQuery);
        statement3.setInt(1, totalGold);
        statement3.setInt(2, totalSilver);
        statement3.setInt(3, totalBronze);
        statement3.setString(4, pays);
        statement3.executeUpdate();
    }

    @FXML
    private void generateCSVReport(MouseEvent event) {
        System.out.println("Generating CSV report...");
        String pays = nomPays.getText();
        String csvFile = "athletes_" + pays + ".csv";
        try (FileWriter out = new FileWriter(csvFile);
             CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader("Pays","Nom", "Prénom", "Âge", "Sport", "Épreuve", "Sexe", "Médailles d'Or", "Médailles d'Argent", "Médailles de Bronze"))) {

            String query = "SELECT a.nom, a.prenom, a.age, a.sport, a.event, a.sexe, " +
                    "SUM(CASE WHEN r.gold = a.id THEN 1 ELSE 0 END) AS gold, " +
                    "SUM(CASE WHEN r.silver = a.id THEN 1 ELSE 0 END) AS silver, " +
                    "SUM(CASE WHEN r.bronze = a.id THEN 1 ELSE 0 END) AS bronze " +
                    "FROM athletes a " +
                    "LEFT JOIN resultats r ON a.id = r.gold OR a.id = r.silver OR a.id = r.bronze " +
                    "INNER JOIN Nationalities n ON a.nationalite = n.nationality " +
                    "WHERE n.country = ? " +
                    "GROUP BY a.id";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, pays);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                printer.printRecord(
                        pays,
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("age"),
                        resultSet.getString("sport"),
                        resultSet.getString("event"),
                        resultSet.getString("sexe"),
                        resultSet.getInt("gold"),
                        resultSet.getInt("silver"),
                        resultSet.getInt("bronze")
                );
            }
            System.out.println("CSV report generated successfully.");

            File file = new File(csvFile);
            Desktop.getDesktop().open(file);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
