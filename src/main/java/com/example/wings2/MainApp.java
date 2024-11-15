package com.example.wings2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.sql.Connection;
import java.util.Objects;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Attempt to connect to the database
        Connection connection = DatabaseConnector.connect();

        // Check if the connection is successful
        if (connection != null) {

            // Load the FXML file if the connection is established
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("LUCT Management System");
            primaryStage.setScene(scene);
            primaryStage.show();
        } else {
            System.out.println("Failed to establish a connection. Exiting application.");
            System.exit(1); // Exit if the connection fails
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}