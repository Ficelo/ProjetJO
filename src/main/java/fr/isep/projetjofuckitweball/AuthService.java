package fr.isep.projetjofuckitweball;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {
    private static int loggedInUserId = -1;

    // Méthode pour définir l'ID de l'utilisateur connecté
    public static void setLoggedInUserId(int userId) {
        loggedInUserId = userId;
        System.out.println("Logged in user ID set to: " + loggedInUserId); // Ajout de log
    }

    // Méthode pour récupérer l'ID de l'utilisateur connecté
    public static int getLoggedInUserId() {
        System.out.println("Retrieving logged in user ID: " + loggedInUserId); // Ajout de log
        return loggedInUserId;
    }

    // Méthode pour vérifier si un utilisateur est connecté
    public static boolean isLoggedIn() {
        return loggedInUserId != -1;
    }

    // Méthode pour récupérer l'ID de l'utilisateur à partir de la base de données
    public static void retrieveUserIdFromDatabase(String username, String password) {
        try {
            DB db = new DB();
            Connection connection = db.getConnection();

            String sql = "SELECT id FROM user WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("id");
                setLoggedInUserId(userId);
                System.out.println(userId);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
