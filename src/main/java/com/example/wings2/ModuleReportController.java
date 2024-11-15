
package com.example.wings2;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ModuleReportController {

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
    @FXML private CheckBox allStudentsRegisteredField;
    @FXML private CheckBox followedUpRegistrationsField;
    @FXML private CheckBox hadGuestLecturerField;
    @FXML private CheckBox planningGuestLecturerField;
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
    }

    private void saveReportToDB() {
        String url = "jdbc:mysql://localhost:3306/AcademicReporting";
        String user = "root";
        String password = "qwert901016824";
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
            pstmt.setBoolean(17, allStudentsRegisteredField.isSelected());
            pstmt.setBoolean(18, followedUpRegistrationsField.isSelected());
            pstmt.setBoolean(19, hadGuestLecturerField.isSelected());
            pstmt.setBoolean(20, planningGuestLecturerField.isSelected());
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

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}