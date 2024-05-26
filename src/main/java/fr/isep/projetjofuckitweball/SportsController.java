package fr.isep.projetjofuckitweball;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class SportsController {

    public Label AcceuilButton;
    public Label SportButton;
    public Label PaysButton;
    public Label AdministrationButton;
    public Label RetourButton;
    private String retourDestination = "";

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
}
