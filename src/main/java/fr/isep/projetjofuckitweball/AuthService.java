package fr.isep.projetjofuckitweball;

public class AuthService {
    private static int loggedInUserId = -1; // Initialisez à une valeur qui ne peut pas être un ID utilisateur valide

    // Méthode pour définir l'ID de l'utilisateur connecté
    public static void setLoggedInUserId(int userId) {
        loggedInUserId = userId;
    }

    // Méthode pour récupérer l'ID de l'utilisateur connecté
    public static int getLoggedInUserId() {
        return loggedInUserId;
    }

    // Méthode pour vérifier si un utilisateur est connecté
    public static boolean isLoggedIn() {
        return loggedInUserId != -1;
    }
}
