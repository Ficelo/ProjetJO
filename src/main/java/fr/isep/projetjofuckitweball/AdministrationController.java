package fr.isep.projetjofuckitweball;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;

public class AdministrationController {

    public Label AcceuilButton;
    public Label SportButton;
    public Label PaysButton;
    public Label AdministrationButton;
    public Label RetourButton;
    public Button connex;

    @FXML private TableView<ResultatScrapping> tableResultat;
    @FXML private TableColumn<ResultatScrapping, String> ResultatEvenement;
    @FXML private TableColumn<ResultatScrapping, String> ResultatGold;
    @FXML private TableColumn<ResultatScrapping, String> ResultatSilver;
    @FXML private TableColumn<ResultatScrapping, String> ResultatBronze;


    private String retourDestination = "";

    private Connection connection;
    @FXML private TableView<PaysScrapping> tableDiscipline;
    @FXML private TableColumn<PaysScrapping, String> DisciplineNom;
    @FXML private TableView<AthleteScrapping> tableAthlete;
    @FXML
    public TableView<EventScrapping> tableEvent;

    @FXML private TableColumn<EventScrapping, String> EventNom;
    @FXML public TableColumn<EventScrapping, String> EventDiscipline;

    @FXML private TableColumn<AthleteScrapping, String> AthleteNom;
    @FXML private TableColumn<AthleteScrapping, String> AthletePrenom;
    @FXML private TableColumn<AthleteScrapping, String> AthleteAge;
    @FXML private TableColumn<AthleteScrapping, String> AthleteSexe;
    @FXML private TableColumn<AthleteScrapping, String> AthleteSport;
    @FXML private TableColumn<AthleteScrapping, String> AthleteEvent;
    @FXML private TableColumn<AthleteScrapping, String> AthleteNationalite;
 @FXML   private ImageView profileImage;
    @FXML
    private TabPane tabPane;


    public void initialize() {
        initializeDB();

        DisciplineNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        EventNom.setCellValueFactory(new PropertyValueFactory<>("nom"));

        //initializePays();
        initializeDiscipline();

        AthleteNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        AthletePrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        AthleteAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        AthleteSexe.setCellValueFactory(new PropertyValueFactory<>("sexe"));
        AthleteSport.setCellValueFactory(new PropertyValueFactory<>("sport"));
        AthleteEvent.setCellValueFactory(new PropertyValueFactory<>("event"));
        AthleteNationalite.setCellValueFactory(new PropertyValueFactory<>("nationalite"));
        initializeAthletes();

        EventNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        EventDiscipline.setCellValueFactory(new PropertyValueFactory<>("discipline_nom"));
        initializeEvent();

        ResultatEvenement.setCellValueFactory(new PropertyValueFactory<>("nom"));
        ResultatGold.setCellValueFactory(new PropertyValueFactory<>("gold"));
        ResultatSilver.setCellValueFactory(new PropertyValueFactory<>("silver"));
        ResultatBronze.setCellValueFactory(new PropertyValueFactory<>("bronze"));

        initializeResultat();

        Improfile();

        // double clic sur athlète
        tableAthlete.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                AthleteScrapping selectedAthlete = tableAthlete.getSelectionModel().getSelectedItem();
                //AthleteScrapping athlete = (AthleteScrapping) event.getSource();
                if (selectedAthlete != null) {
                    handleDoubleClickOnAthlete(selectedAthlete);
                }
            }
        });

        // double clic sur discipline
        tableDiscipline.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                PaysScrapping selectedDiscipline = tableDiscipline.getSelectionModel().getSelectedItem();
                if (selectedDiscipline != null) {
                    handleDoubleClickOnDisc(selectedDiscipline);
                }
            }
        });

        //double click evenement
        tableEvent.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                EventScrapping selectedEvent = tableEvent.getSelectionModel().getSelectedItem();
                if (selectedEvent != null) {
                    handleDoubleClickOnEvent(selectedEvent);
                }
            }
        });


    }

    public void initializeAthletes(){
        String query = "SELECT nom, prenom, age, sexe, sport, event, nationalite FROM athletes";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            int count=0;
            ObservableList<AthleteScrapping> athletes = FXCollections.observableArrayList();

            while (resultSet.next()) {
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String age = resultSet.getString("age");
                String sexe = resultSet.getString("sexe");
                String sport = resultSet.getString("sport");
                String event = resultSet.getString("event");
                String nationalite = resultSet.getString("nationalite");
                athletes.add(new AthleteScrapping(nom, prenom, age, sexe, sport, event, nationalite));
                count++; // Incrémente le compteur à chaque athlète récupéré

            }

            System.out.println("nombre d'athletes "+ count);

            tableAthlete.setItems(athletes);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /*public void initializePays(){
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
    */
    public void initializeDiscipline(){
        String query = "SELECT nom FROM discipline";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            ObservableList<PaysScrapping> discipline = FXCollections.observableArrayList();

            while (resultSet.next()) {
                String nom = resultSet.getString("nom");
                discipline.add(new PaysScrapping(nom));
            }

            tableDiscipline.setItems(discipline);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initializeResultat() {
        String query = "SELECT r.evenement_id, e.nom AS evenement_nom, r.bronze, r.silver, r.gold " +
                "FROM resultats r " +
                "JOIN evenement e ON r.evenement_id = e.id";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            ObservableList<ResultatScrapping> resultats = FXCollections.observableArrayList();

            while (resultSet.next()) {
                int evenementId = resultSet.getInt("evenement_id");
                String evenementNom = resultSet.getString("evenement_nom");
                int bronzeAthleteId = resultSet.getInt("bronze");
                int silverAthleteId = resultSet.getInt("silver");
                int goldAthleteId = resultSet.getInt("gold");

                String bronzeAthleteName = fetchAthleteName(bronzeAthleteId);
                String silverAthleteName = fetchAthleteName(silverAthleteId);
                String goldAthleteName = fetchAthleteName(goldAthleteId);

                resultats.add(new ResultatScrapping(evenementNom, bronzeAthleteName, silverAthleteName, goldAthleteName));
            }

            ResultatEvenement.setCellValueFactory(new PropertyValueFactory<>("nom"));
            ResultatBronze.setCellValueFactory(new PropertyValueFactory<>("bronze"));
            ResultatSilver.setCellValueFactory(new PropertyValueFactory<>("silver"));
            ResultatGold.setCellValueFactory(new PropertyValueFactory<>("gold"));

            tableResultat.setItems(resultats);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String fetchAthleteName(int athleteId) throws SQLException {
        String query = "SELECT nom FROM athletes WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, athleteId);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString("nom");
        } else {
            return "Unknown";
        }
    }

    public void initializeEvent() {
        String query = "SELECT e.nom AS event_nom, d.nom AS discipline_nom " +
                "FROM evenement e " +
                "JOIN discipline d ON e.discipline_id = d.id";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            ObservableList<EventScrapping> events = FXCollections.observableArrayList();

            while (resultSet.next()) {
                String eventNom = resultSet.getString("event_nom");
                String disciplineNom = resultSet.getString("discipline_nom");
                events.add(new EventScrapping(eventNom, disciplineNom));
            }

            tableEvent.setItems(events);
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

    public void updateRetour(String retourDest){
        this.retourDestination = retourDest;
    }

    private void Improfile()
    {
        try {
            DB db = new DB();
            Connection connection = db.getConnection();
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
                    profileImage.setImage(image);
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
    public void add(ActionEvent actionEvent) {
        if (tabPane.getSelectionModel().getSelectedItem().getText().equals("Athletes")) {
            //showAlert(Alert.AlertType.INFORMATION, "athlete", "je suis biens sur les ath.");
            redirectionath();

        } else if (tabPane.getSelectionModel().getSelectedItem().getText().equals("Discipline")) {
            //showAlert(Alert.AlertType.INFORMATION, "Discipline", "je suis biens sur les disc.");
            redirectiondisc();
        } else if (tabPane.getSelectionModel().getSelectedItem().getText().equals("Evenement")) {
            //showAlert(Alert.AlertType.INFORMATION, "event", "je suis biens sur les event.");
            redirectionevent();
        } else {
            //showAlert(Alert.AlertType.INFORMATION, "Résultat", "je suis biens sur les résultats.");
            redirectionAjoutResultat();
        }

        }

    private void redirectionAjoutResultat() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ajoutResultat.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) connex.getScene().getWindow();
            stage.close();

            Stage inscriptionStage = new Stage();
            inscriptionStage.setScene(scene);
            inscriptionStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void redirectionath()
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Athletes.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) connex.getScene().getWindow();
            stage.close();

            Stage inscriptionStage = new Stage();
            inscriptionStage.setScene(scene);
            inscriptionStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void redirectiondisc()
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Disc.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) connex.getScene().getWindow();
            stage.close();

            Stage inscriptionStage = new Stage();
            inscriptionStage.setScene(scene);
            inscriptionStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void redirectionevent()
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("event.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) connex.getScene().getWindow();
            stage.close();

            Stage inscriptionStage = new Stage();
            inscriptionStage.setScene(scene);
            inscriptionStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void handleDoubleClickOnAthlete(AthleteScrapping athlete) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modifAthlete.fxml"));
            Parent root = loader.load();

            modifAthleteControlleur controller = loader.getController();

            controller.setAthlete(athlete);

            Scene scene = new Scene(root);
            Stage stage = (Stage) connex.getScene().getWindow();
            stage.close();
            Stage inscriptionStage = new Stage();
            inscriptionStage.setScene(scene);
            inscriptionStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDoubleClickOnDisc(PaysScrapping discipline) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modifDiscipline.fxml"));
            Parent root = loader.load();

            ModifDisciplineControlleur controller = loader.getController();

            controller.setDiscipline(discipline);

            Scene scene = new Scene(root);
            Stage stage = (Stage) connex.getScene().getWindow();
            stage.close();

            Stage inscriptionStage = new Stage();
            inscriptionStage.setScene(scene);
            inscriptionStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void handleDoubleClickOnEvent(EventScrapping event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifEvent.fxml"));
            Parent root = loader.load();

            ModifEventControlleur controller = loader.getController();

            controller.setEvent(event);

            Scene scene = new Scene(root);
            Stage stage = (Stage) connex.getScene().getWindow();
            stage.close();

            Stage inscriptionStage = new Stage();
            inscriptionStage.setScene(scene);
            inscriptionStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
