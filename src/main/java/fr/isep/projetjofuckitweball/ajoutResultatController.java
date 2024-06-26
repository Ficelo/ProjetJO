package fr.isep.projetjofuckitweball;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ajoutResultatController {

    public Label AcceuilButton;
    public Label SportButton;
    public Label PaysButton;
    public Label AdministrationButton;
    public Label RetourButton;
    public ChoiceBox choiceEvenement;
    public ChoiceBox choiceGold;
    public ChoiceBox choiceSilver;
    public ChoiceBox choiceBronze;
    public Button ajouterButton;
    private Connection connection;

    private HashMap<String, Integer> athletesID = new HashMap<>();
    private HashMap<String, Integer> evenementID = new HashMap<>();

    public void initialize() {

        connection = DB.getConnection();
        populateEvenementChoiceBox();
        populateAthleteChoiceBoxes();
        
    }

    private void populateEvenementChoiceBox() {

        choiceEvenement.getItems().clear();

        String query = "SELECT id, nom FROM evenement";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            List<String> evenements = new ArrayList<>();

            while (resultSet.next()) {
                String nom = resultSet.getString("nom");
                int id = resultSet.getInt("id");
                evenementID.put(nom, id);
                evenements.add(nom);
            }

            choiceEvenement.setItems(FXCollections.observableArrayList(evenements));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateAthleteChoiceBoxes() {
        choiceGold.getItems().clear();
        choiceSilver.getItems().clear();
        choiceBronze.getItems().clear();

        String query = "SELECT id, CONCAT(prenom, ' ', nom) AS fullname FROM athletes ORDER BY nom, prenom";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            List<String> athletes = new ArrayList<>();

            while (resultSet.next()) {
                String fullname = resultSet.getString("fullname");
                int id = resultSet.getInt("id");
                athletesID.put(fullname, id);
                athletes.add(fullname);
            }

            ObservableList<String> athleteList = FXCollections.observableArrayList(athletes);
            choiceGold.setItems(athleteList);
            choiceSilver.setItems(athleteList);
            choiceBronze.setItems(athleteList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add(ActionEvent actionEvent) throws IOException, SQLException {
        System.out.println("Evenement : " + choiceEvenement.getValue() + " Gold : " + choiceGold.getValue() + " Silver : " + choiceSilver.getValue() + " Bronze : " + choiceBronze.getValue());
        String query = "INSERT INTO resultats (evenement_id, gold, silver, bronze) VALUES (?, ?, ?, ?)";

        ArrayList<String> countries = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, evenementID.get(choiceEvenement.getValue()));
            statement.setInt(2, athletesID.get(choiceGold.getValue()));
            statement.setInt(3, athletesID.get(choiceSilver.getValue()));
            statement.setInt(4, athletesID.get(choiceBronze.getValue()));

            String goldCountry = getCountryByNationality(choiceGold.getValue().toString());
            String silverCountry = getCountryByNationality(choiceSilver.getValue().toString());
            String bronzeCountry = getCountryByNationality(choiceBronze.getValue().toString());

            System.out.println("Gold Athlete Country: " + goldCountry);
            System.out.println("Silver Athlete Country: " + silverCountry);
            System.out.println("Bronze Athlete Country: " + bronzeCountry);

            countries.add(goldCountry);
            countries.add(silverCountry);
            countries.add(bronzeCountry);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Administration.fxml"));
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) AcceuilButton.getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // C'est une méthode de maxi barbare de faire comme ça mais je reverai ça plus tard

        for(String p : countries){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PaysIndividuel.fxml"));
            Parent root = fxmlLoader.load();
            PaysIndividuelController controller = fxmlLoader.getController();
            controller.updateNomPays(p);
        }



    }

    private String getCountryByNationality(String athleteName) {
        String[] names = athleteName.split(" ");
        if (names.length < 2) {
            System.out.println("Invalid athlete name: " + athleteName);
            return null;
        }
        String firstName = names[0];
        String lastName = names[1];

        String query = "SELECT p.nom AS country " +
                "FROM Nationalities n " +
                "JOIN athletes a ON a.nationalite = n.nationality " +
                "JOIN pays p ON n.country = p.nom " +
                "WHERE a.prenom = ? AND a.nom = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            System.out.println(statement);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("country");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Administration.fxml"));
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
