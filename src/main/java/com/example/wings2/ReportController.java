package com.example.wings2;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;

import java.io.IOException;

public class ReportController {
    public TextArea challengesField; // Make sure to define these if you need to access them
    public TextArea recommendationsField;

    public void handleReportSubmission(ActionEvent event) {
        // Logic to handle report submission
    }

    public void handleLogout(ActionEvent event) {
        try {
            // Load the Home page FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml")); // Adjust path as necessary
            Parent root = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) challengesField.getScene().getWindow(); // Use any control to get the current stage
            stage.setScene(new Scene(root));
            stage.setTitle("Home"); // Optional: Set the window title
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Optionally, show an error message to the user
        }
    }
}