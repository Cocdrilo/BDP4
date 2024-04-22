module edu.bd4.bdp4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens edu.bd4.bdp4 to javafx.fxml;
    exports edu.bd4.bdp4;
}