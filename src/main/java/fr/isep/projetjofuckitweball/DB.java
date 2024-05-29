package fr.isep.projetjofuckitweball;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class DB {
    public static Connection DbLink;


    public static Connection getConnection()
    {
        //test

        String databaseName="app_java";
        String databaseUser="root";
        String databasePassword="";
        String url ="jdbc:mysql://localhost/"+databaseName;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DbLink =DriverManager.getConnection(url,databaseUser,databasePassword);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return DbLink;
    }
    public PreparedStatement prepareStatement(String query) throws SQLException {
        return DbLink.prepareStatement(query);
    }
    public void closeConnection() {
        try {
            if (DbLink != null) {
                DbLink.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
