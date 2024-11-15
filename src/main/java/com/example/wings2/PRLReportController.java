package com.example.wings2;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class PRLReportController {

    @FXML private TableView<ReportEntry> reportTable;
    @FXML private TableColumn<ReportEntry, String> lecturerColumn;
    @FXML private TableColumn<ReportEntry, String> classColumn;
    @FXML private TableColumn<ReportEntry, String> courseColumn;
    @FXML private TableColumn<ReportEntry, String> chapterColumn;
    @FXML private TableColumn<ReportEntry, String> deliveryModeColumn;
    @FXML private TableColumn<ReportEntry, Integer> missedClassesColumn;
    @FXML private TableColumn<ReportEntry, Integer> attendanceColumn;
    @FXML private TableColumn<ReportEntry, Integer> registrationsColumn;
    @FXML private TableColumn<ReportEntry, String> challengesColumn;
    @FXML private TableColumn<ReportEntry, String> recommendationsColumn;
    @FXML private TableColumn<ReportEntry, String> malpracticeColumn;

    @FXML private TextField lecturerField;
    @FXML private TextField classField;
    @FXML private TextField courseField;
    @FXML private TextField chapterField;
    @FXML private TextField modeField;
    @FXML private TextField missedClassesField;
    @FXML private TextField attendanceField;
    @FXML private TextField registrationsField;
    @FXML private TextArea challengesField;
    @FXML private TextArea recommendationsField;
    @FXML private TextArea malpracticeField;

    private ObservableList<ReportEntry> reportData = FXCollections.observableArrayList();
    private ReportDAO reportDAO = new ReportDAO();

    @FXML
    public void initialize() {
        // Initialize the TableView columns
        lecturerColumn.setCellValueFactory(new PropertyValueFactory<>("lecturer"));
        classColumn.setCellValueFactory(new PropertyValueFactory<>("className"));
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        chapterColumn.setCellValueFactory(new PropertyValueFactory<>("chapterCovered"));
        deliveryModeColumn.setCellValueFactory(new PropertyValueFactory<>("modeOfDelivery"));
        missedClassesColumn.setCellValueFactory(new PropertyValueFactory<>("missedClasses"));
        attendanceColumn.setCellValueFactory(new PropertyValueFactory<>("studentAttendance"));
        registrationsColumn.setCellValueFactory(new PropertyValueFactory<>("studentRegistrations"));
        challengesColumn.setCellValueFactory(new PropertyValueFactory<>("challenges"));
        recommendationsColumn.setCellValueFactory(new PropertyValueFactory<>("recommendations"));
        malpracticeColumn.setCellValueFactory(new PropertyValueFactory<>("malpractice"));

        // Load data from the database
        loadReportData();

        // Show welcome message
        showWelcomeMessage();
    }

    private void loadReportData() {
        try {
            List<ReportEntry> entries = reportDAO.getAllReportEntries();
            reportData.clear();
            reportData.addAll(entries);
            reportTable.setItems(reportData);
        } catch (SQLException e) {
            showErrorDialog("Database Error", "Failed to load report data: " + e.getMessage());
        }
    }

    @FXML
    public void handleAddEntry() {
        // Collect data from input fields
        String lecturer = lecturerField.getText();
        String className = classField.getText();
        String courseName = courseField.getText();
        String chapterCovered = chapterField.getText();
        String modeOfDelivery = modeField.getText();

        // Validate and parse integer fields
        try {
            int missedClasses = Integer.parseInt(missedClassesField.getText());
            int attendance = Integer.parseInt(attendanceField.getText());
            int registrations = Integer.parseInt(registrationsField.getText());

            // Call the method to add new entry
            addNewEntry(lecturer, className, courseName, chapterCovered, modeOfDelivery,
                    missedClasses, attendance, registrations, challengesField.getText(),
                    recommendationsField.getText(), malpracticeField.getText());
        } catch (NumberFormatException e) {
            showErrorDialog("Input Error", "Please enter valid numbers for missed classes, attendance, and registrations.");
        }
    }

    public void addNewEntry(String lecturer, String className, String courseName,
                            String chapterCovered, String modeOfDelivery,
                            int missedClasses, int attendance,
                            int registrations, String challenges,
                            String recommendations, String malpractice) {
        try {
            reportDAO.insertReportEntry(lecturer, className, courseName, chapterCovered,
                    modeOfDelivery, missedClasses, attendance,
                    registrations, challenges, recommendations, malpractice);
            loadReportData(); // Refresh the table after insertion
            clearInputFields(); // Clear the input fields after adding
        } catch (SQLException e) {
            showErrorDialog("Database Error", "Failed to insert data: " + e.getMessage());
        }
    }

    private void clearInputFields() {
        lecturerField.clear();
        classField.clear();
        courseField.clear();
        chapterField.clear();
        modeField.clear();
        missedClassesField.clear();
        attendanceField.clear();
        registrationsField.clear();
        challengesField.clear();
        recommendationsField.clear();
        malpracticeField.clear();
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Show a welcome message to the user
    private void showWelcomeMessage() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Welcome");
        alert.setHeaderText("Welcome to the PRL Report System");
        alert.setContentText("You have successfully logged in. Please enter your report entries.");
        alert.showAndWait();
    }

    // Handle Back button action
    @FXML
    public void handleBack() {
        // Logic to return to the home page
        System.out.println("Going back to the home page...");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml")); // Adjust the path as necessary
            Parent homeRoot = loader.load();
            Stage stage = (Stage) reportTable.getScene().getWindow(); // Get the current window
            stage.setScene(new Scene(homeRoot)); // Set the new scene
            stage.setTitle("Home Page");
            stage.show();
        } catch (IOException e) {
            showErrorDialog("Navigation Error", "Failed to load home page: " + e.getMessage());
        }
    }

    // Handle Logout button action
    @FXML
    public void handleLogout() {
        // Logic to go to the login page
        System.out.println("Logging out...");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml")); // Adjust the path as necessary
            Parent loginRoot = loader.load();
            Stage stage = (Stage) reportTable.getScene().getWindow(); // Get the current window
            stage.setScene(new Scene(loginRoot)); // Set the new scene
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            showErrorDialog("Navigation Error", "Failed to load login page: " + e.getMessage());
        }
    }
}