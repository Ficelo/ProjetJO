package fr.isep.projetjofuckitweball;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class AdministrationController {

    public Label AcceuilButton;
    public Label SportButton;
    public Label PaysButton;
    public Label AdministrationButton;
    public Label RetourButton;
    public Button connex;
    private String retourDestination = "";

    private Connection connection;
    @FXML private TableView<PaysScrapping> tableDiscipline;
    @FXML private TableColumn<PaysScrapping, String> DisciplineNom;
    @FXML private TableView<AthleteScrapping> tableAthlete;
    @FXML private TableColumn<AthleteScrapping, String> AthleteNom;
    @FXML private TableColumn<AthleteScrapping, String> AthletePrenom;
    @FXML private TableColumn<AthleteScrapping, String> AthleteAge;
    @FXML private TableColumn<AthleteScrapping, String> AthleteSexe;
    @FXML private TableColumn<AthleteScrapping, String> AthleteSport;
    @FXML private TableColumn<AthleteScrapping, String> AthleteEvent;
    @FXML private TableColumn<AthleteScrapping, String> AthleteNationalite;

    public void initialize() {
        initializeDB();

        DisciplineNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        initializePays();

        AthleteNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        AthletePrenom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        AthleteAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        AthleteSexe.setCellValueFactory(new PropertyValueFactory<>("sexe"));
        AthleteSport.setCellValueFactory(new PropertyValueFactory<>("sport"));
        AthleteEvent.setCellValueFactory(new PropertyValueFactory<>("event"));
        AthleteNationalite.setCellValueFactory(new PropertyValueFactory<>("nationalite"));
        initializeAthletes();


    }

    public void initializeAthletes(){
        String query = "SELECT nom, prenom, age, sport, event, nationalite, sexe FROM athletes";  // Sélectionne uniquement la colonne "nom"
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            ObservableList<AthleteScrapping> athletes = FXCollections.observableArrayList();

            while (resultSet.next()) {
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String age = resultSet.getString("age");
                String sport = resultSet.getString("sport");
                String event = resultSet.getString("event");
                String nationalite = resultSet.getString("nationalite");
                String sexe = resultSet.getString("sexe");
                athletes.add(new AthleteScrapping(nom, prenom, sport, age, sexe, nationalite, event));
            }

            tableAthlete.setItems(athletes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initializePays(){
        String query = "SELECT nom FROM pays";  // Sélectionne uniquement la colonne "nom"
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            ObservableList<PaysScrapping> countries = FXCollections.observableArrayList();

            while (resultSet.next()) {
                String nom = resultSet.getString("nom");
                countries.add(new PaysScrapping(nom));
            }

            tableDiscipline.setItems(countries);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            controlleur.updateRetour("Administration");

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


            SportController controlleur = fxmlLoader.getController();
            controlleur.updateRetour("Administration");

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
            controlleur.updateRetour("Administration");

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
            controlleur.updateRetour("Administration");

            stage.setScene(scene);
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadRetour(MouseEvent mouseEvent) {
        loadAcceuil(mouseEvent);
    }

    public void loginUser(ActionEvent actionEvent) {
    }

    public void updateRetour(String retourDest){
        this.retourDestination = retourDest;
    }
}
