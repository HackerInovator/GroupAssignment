package com.example.wings2;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;

public class HomeController {
    @FXML private Label errorLabel;

    @FXML
    private void handleAdminLogin() {
        loadPanel("admin.fxml");
        showWelcomeMessage(); // Show welcome message after loading the admin panel
    }

    @FXML
    private void handleLecturerLogin() {
        loadPanel("lecturers.fxml");
        showWelcomeMessage(); // Show welcome message after loading the lecturer panel
    }

    @FXML
    private void handlePRLogin() {
        loadPanel("prl.fxml");
        showWelcomeMessage(); // Show welcome message after loading the PR panel
    }

    @FXML
    private void handleLogout() {
        loadPanel("login.fxml"); // Load the login panel
    }

    private void loadPanel(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) errorLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Academic Reporting System - " + fxmlFile.replace(".fxml", ""));
            stage.show();
        } catch (IOException e) {
            showError("Error loading panel: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWelcomeMessage() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Welcome");
        alert.setHeaderText(null);
        alert.setContentText("Welcome to the Academic Reporting System!");
        alert.showAndWait();
    }
}