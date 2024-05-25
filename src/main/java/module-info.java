module fr.isep.projetjofuckitweball {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires org.jsoup;
    requires java.desktop;


    opens fr.isep.projetjofuckitweball to javafx.fxml;
    exports fr.isep.projetjofuckitweball;
}