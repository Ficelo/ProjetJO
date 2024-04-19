module fr.isep.projetjofuckitweball {
    requires javafx.controls;
    requires javafx.fxml;


    opens fr.isep.projetjofuckitweball to javafx.fxml;
    exports fr.isep.projetjofuckitweball;
}