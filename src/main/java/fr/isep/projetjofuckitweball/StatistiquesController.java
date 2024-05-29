package fr.isep.projetjofuckitweball;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class StatistiquesController {
    public Label AcceuilButton;
    public Label SportButton;
    public Label PaysButton;
    public Label AdministrationButton;
    public Label RetourButton;
    public LineChart graphique;
    private Connection connection;

    public void initialize() {

        connection = DB.getConnection();

        fetchAndDisplayData();
    }

    private void fetchAndDisplayData() {
        String query = "SELECT date, pays1, pays2, pays3, pays4, pays5, medals1, medals2, medals3, medals4, medals5 FROM ranking";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // A map to hold aggregated medal counts per date for each country
            Map<String, Map<String, Integer>> countryMedalCounts = new HashMap<>();

            while (resultSet.next()) {
                String date = resultSet.getString("date");

                // Iterate over each country and its corresponding medals
                for (int i = 1; i <= 5; i++) {
                    String country = resultSet.getString("pays" + i);
                    String[] medals = resultSet.getString("medals" + i).split(";");
                    int goldMedals = Integer.parseInt(medals[0]);

                    // Update the medal count for the current country and date
                    Map<String, Integer> medalCounts = countryMedalCounts.computeIfAbsent(country, k -> new HashMap<>());
                    medalCounts.put(date, medalCounts.getOrDefault(date, 0) + goldMedals);
                }
            }

            resultSet.close();
            statement.close();

            // Prepare data for the LineChart
            graphique.getData().clear();

            for (String country : countryMedalCounts.keySet()) {
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName(country);

                // Sort the dates to ensure the x-axis is in chronological order
                List<String> sortedDates = new ArrayList<>(countryMedalCounts.get(country).keySet());
                sortedDates.sort(Comparator.naturalOrder());

                for (String date : sortedDates) {
                    series.getData().add(new XYChart.Data<>(date, countryMedalCounts.get(country).get(date)));
                }

                // Add the series to the chart
                graphique.getData().add(series);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    private static class CountryMedals {
        private final String country;
        private final int goldMedals;

        public CountryMedals(String country, int goldMedals) {
            this.country = country;
            this.goldMedals = goldMedals;
        }

        public String getCountry() {
            return country;
        }

        public int getGoldMedals() {
            return goldMedals;
        }
    }

    public void loadConnexion(MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Connexion.fxml"));
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) AdministrationButton.getScene().getWindow();

            HelloController controlleur = fxmlLoader.getController();
            controlleur.updateRetour("Principale");

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
            controlleur.updateRetour("Principale");

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
            controlleur.updateRetour("Principale");

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
            controlleur.updateRetour("Principale");

            stage.setScene(scene);
            stage.show();



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadRetour(MouseEvent mouseEvent) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Principale.fxml"));
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) RetourButton.getScene().getWindow();

            stage.setScene(scene);
            stage.show();
    }
}

