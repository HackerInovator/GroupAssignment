package com.example.wings2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LecturerController {

    @FXML private TextField facultyField;
    @FXML private TextField moduleCodeField;
    @FXML private TextField classField;
    @FXML private TextField lecturerField;
    @FXML private TextField weekField;
    @FXML private TextField topicField;
    @FXML private TextField learningOutcomeField;
    @FXML private TextField assessmentNameField;
    @FXML private TextField passRateField;
    @FXML private TextField challengesField;
    @FXML private TextField durationField;
    @FXML private TextField recommendationsField;
    @FXML private TextField studentsField;
    @FXML private TextField absentStudentsField;
    @FXML private TextField classAttendanceField;
    @FXML private TextField attendanceLevelField;

    // Updated to ComboBox
    @FXML private ComboBox<String> allStudentsRegisteredField;
    @FXML private ComboBox<String> followedUpRegistrationsField;
    @FXML private ComboBox<String> hadGuestLecturerField;
    @FXML private ComboBox<String> planningGuestLecturerField;

    @FXML private TextField guestLecturerTopicField;
    @FXML private TextField studentsWithLearningNeedsField;
    @FXML private TextField instancesOfIndisciplineField;
    @FXML private TextField studentAchievementsField;
    @FXML private TextField assessmentMalpracticeField;

    @FXML
    private void handleSaveReport() {
        // Save the report data to the database
        saveReportToDB();
    }

    @FXML
    private void handleSubmitReport() {
        // Submit the report to the administration
        submitReport();
        // Clear all fields after submission
        clearFields();
    }

    private void saveReportToDB() {
        String url = "jdbc:mysql://localhost:3306/academic_reporting";
        String user = "root";
        String password = "2003v400";
        String query = "INSERT INTO module_reports (faculty, module_code, class_name, lecturer, week, topic, learning_outcome, " +
                "assessment_name, pass_rate, challenges, duration, recommendations, students, absent_students, " +
                "class_attendance, attendance_level, all_students_registered, followed_up_registrations, " +
                "had_guest_lecturer, planning_guest_lecturer, guest_lecturer_topic, students_with_learning_needs, " +
                "instances_of_indiscipline, student_achievements, assessment_malpractice) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, facultyField.getText());
            pstmt.setString(2, moduleCodeField.getText());
            pstmt.setString(3, classField.getText());
            pstmt.setString(4, lecturerField.getText());
            pstmt.setString(5, weekField.getText());
            pstmt.setString(6, topicField.getText());
            pstmt.setString(7, learningOutcomeField.getText());
            pstmt.setString(8, assessmentNameField.getText());
            pstmt.setInt(9, Integer.parseInt(passRateField.getText()));
            pstmt.setString(10, challengesField.getText());
            pstmt.setString(11, durationField.getText());
            pstmt.setString(12, recommendationsField.getText());
            pstmt.setInt(13, Integer.parseInt(studentsField.getText()));
            pstmt.setInt(14, Integer.parseInt(absentStudentsField.getText()));
            pstmt.setString(15, classAttendanceField.getText());
            pstmt.setString(16, attendanceLevelField.getText());

            // Updated to get selected values from ComboBoxes
            pstmt.setBoolean(17, "Yes".equals(allStudentsRegisteredField.getValue()));
            pstmt.setBoolean(18, "Yes".equals(followedUpRegistrationsField.getValue()));
            pstmt.setBoolean(19, "Yes".equals(hadGuestLecturerField.getValue()));
            pstmt.setBoolean(20, "Yes".equals(planningGuestLecturerField.getValue()));

            pstmt.setString(21, guestLecturerTopicField.getText());
            pstmt.setString(22, studentsWithLearningNeedsField.getText());
            pstmt.setString(23, instancesOfIndisciplineField.getText());
            pstmt.setString(24, studentAchievementsField.getText());
            pstmt.setString(25, assessmentMalpracticeField.getText());

            pstmt.executeUpdate();

            // Show a success alert
            showAlert(Alert.AlertType.INFORMATION, "Success", "Report saved successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save report: " + e.getMessage());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter valid numbers for pass rate, students, and absent students.");
        }
    }

    private void submitReport() {
        // Submit the report to the administration
        // You can add any additional logic or notifications here
        showAlert(Alert.AlertType.INFORMATION, "Success", "Report submitted successfully!");
    }

    private void clearFields() {
        facultyField.clear();
        moduleCodeField.clear();
        classField.clear();
        lecturerField.clear();
        weekField.clear();
        topicField.clear();
        learningOutcomeField.clear();
        assessmentNameField.clear();
        passRateField.clear();
        challengesField.clear();
        durationField.clear();
        recommendationsField.clear();
        studentsField.clear();
        absentStudentsField.clear();
        classAttendanceField.clear();
        attendanceLevelField.clear();

        // Reset ComboBoxes
        allStudentsRegisteredField.setValue(null);
        followedUpRegistrationsField.setValue(null);
        hadGuestLecturerField.setValue(null);
        planningGuestLecturerField.setValue(null);

        guestLecturerTopicField.clear();
        studentsWithLearningNeedsField.clear();
        instancesOfIndisciplineField.clear();
        studentAchievementsField.clear();
        assessmentMalpracticeField.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleBack() {
        try {
            // Load the home page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) facultyField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Home Page");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load home page: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        try {
            // Load the login page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) facultyField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login Page");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load login page: " + e.getMessage());
        }
    }
}