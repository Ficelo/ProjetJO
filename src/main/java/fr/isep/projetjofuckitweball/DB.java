package fr.isep.projetjofuckitweball;

import java.sql.Connection;
import java.sql.DriverManager;


public class DB {
    public Connection DbLink;

    public Connection getConnection()
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
}
