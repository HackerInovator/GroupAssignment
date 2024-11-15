module com.example.wings2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens com.example.wings2 to javafx.fxml;
    exports com.example.wings2;
}