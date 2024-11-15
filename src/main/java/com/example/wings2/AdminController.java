package com.example.wings2;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class AdminController {
    @FXML private TextField lecturerUsernameField;
    @FXML private TextField lecturerNameField;
    @FXML private TextField lecturerSurnameField;
    @FXML private TextField lecturerEmailField;
    @FXML private TextField lecturerAddressField;
    @FXML private TextField employeeNumberField;
    @FXML private ListView<String> lecturersListView;
    @FXML private Label addErrorLabel;
    @FXML private TextField academicYearField;
    @FXML private TextField semesterField;
    @FXML private TextField moduleField;
    @FXML private TextField classField;
    @FXML private TextField searchField;
    @FXML private Label adminProfileLabel;
    @FXML private Label studentsListLabel;

    // New fields for adding a student
    @FXML private TextField studentNameField;
    @FXML private TextField studentSurnameField;
    @FXML private TextField studentNumberField;
    @FXML private TextField studentEmailField;
    @FXML private TextField studentContactsField;
    @FXML private Label addStudentErrorLabel;

    // New fields for assigning role and module
    @FXML private ComboBox<String> lecturerRoleComboBox;
    @FXML private TextField newRoleField;
    @FXML private Label roleAssignErrorLabel;
    @FXML private ComboBox<String> lecturerModuleComboBox;
    @FXML private TextField moduleAssignField;
    @FXML private TextField classAssignField;
    @FXML private TextField semesterAssignField;
    @FXML private TextField academicYearAssignField;
    @FXML private Label moduleAssignErrorLabel;

    private String loggedInAdminUsername = "admin"; // Set to the actual logged-in username
    private List<String> lecturers;

    @FXML
    public void initialize() {
        loadExistingLecturers();
        displayAdminProfile();
    }

    private void displayAdminProfile() {
        adminProfileLabel.setText("Welcome, " + loggedInAdminUsername);
    }

    @FXML
    private void viewProfile() {
        StringBuilder content = new StringBuilder();
        try (Connection connection = DatabaseConnector.connect()) {
            String query = "SELECT username, password, role FROM users WHERE username = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, loggedInAdminUsername);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password"); // Ideally, don't show passwords
                String role = rs.getString("role");

                content.append("Username: ").append(username)
                        .append("\nPassword: ").append(password) // Displaying password directly is not recommended
                        .append("\nRole: ").append(role);
            } else {
                content.append("User not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            content.append("Error loading profile: ").append(e.getMessage());
        }

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Admin Profile");
        alert.setHeaderText("Admin Profile Information");
        alert.setContentText(content.toString());
        alert.showAndWait();
    }

    private void loadExistingLecturers() {
        lecturersListView.getItems().clear();
        lecturerRoleComboBox.getItems().clear();
        lecturerModuleComboBox.getItems().clear();
        lecturers = new ArrayList<>();
        try (Connection connection = DatabaseConnector.connect()) {
            String query = "SELECT username, name, surname FROM lecturers";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String fullName = rs.getString("username") + ": " + rs.getString("name") + " " + rs.getString("surname");
                lecturers.add(fullName);
                lecturersListView.getItems().add(fullName);
                lecturerRoleComboBox.getItems().add(fullName); // Populate role ComboBox
                lecturerModuleComboBox.getItems().add(fullName); // Populate module ComboBox
            }
        } catch (SQLException e) {
            e.printStackTrace();
            addErrorLabel.setText("Error loading lecturers: " + e.getMessage());
        }
    }

    @FXML
    private void searchLecturers() {
        String query = searchField.getText().toLowerCase();
        lecturersListView.getItems().clear();

        for (String lecturer : lecturers) {
            if (lecturer.toLowerCase().contains(query)) {
                lecturersListView.getItems().add(lecturer);
            }
        }

        if (lecturersListView.getItems().isEmpty()) {
            addErrorLabel.setText("No results found.");
        } else {
            addErrorLabel.setText("");
        }
    }

    @FXML
    private void addLecturer() {
        String username = lecturerUsernameField.getText();
        String name = lecturerNameField.getText();
        String surname = lecturerSurnameField.getText();
        String email = lecturerEmailField.getText();
        String address = lecturerAddressField.getText();
        String employeeNumber = employeeNumberField.getText();

        if (username.isEmpty() || name.isEmpty() || surname.isEmpty() || email.isEmpty() || address.isEmpty() || employeeNumber.isEmpty()) {
            addErrorLabel.setText("All fields must be filled out.");
            return;
        }

        try (Connection connection = DatabaseConnector.connect()) {
            String query = "INSERT INTO lecturers (username, name, surname, email, address, employee_number, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, name);
            stmt.setString(3, surname);
            stmt.setString(4, email);
            stmt.setString(5, address);
            stmt.setString(6, employeeNumber);
            stmt.setString(7, employeeNumber); // Assuming same as employee number for password
            stmt.executeUpdate();

            clearLecturerFields();
            loadExistingLecturers(); // Refresh the list after adding
        } catch (SQLException e) {
            e.printStackTrace();
            addErrorLabel.setText("Error adding lecturer: " + e.getMessage());
        }
    }

    private void clearLecturerFields() {
        lecturerUsernameField.clear();
        lecturerNameField.clear();
        lecturerSurnameField.clear();
        lecturerEmailField.clear();
        lecturerAddressField.clear();
        employeeNumberField.clear();
    }

    @FXML
    private void assignRole() {
        String selectedLecturer = lecturerRoleComboBox.getValue();
        String newRole = newRoleField.getText();

        if (selectedLecturer == null || newRole.isEmpty()) {
            roleAssignErrorLabel.setText("Please select a lecturer and enter a new role.");
            return;
        }

        String username = selectedLecturer.split(":")[0]; // Extract username from the selected string

        try (Connection connection = DatabaseConnector.connect()) {
            String query = "UPDATE lecturers SET role = ? WHERE username = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, newRole);
            stmt.setString(2, username);
            stmt.executeUpdate();

            newRoleField.clear();
            roleAssignErrorLabel.setText("Role assigned successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            roleAssignErrorLabel.setText("Error assigning role: " + e.getMessage());
        }
    }

    @FXML
    private void assignModule() {
        String selectedLecturer = lecturerModuleComboBox.getValue();
        String moduleName = moduleAssignField.getText();
        String className = classAssignField.getText();
        String semester = semesterAssignField.getText();
        String academicYear = academicYearAssignField.getText();

        if (selectedLecturer == null || moduleName.isEmpty() || className.isEmpty() || semester.isEmpty() || academicYear.isEmpty()) {
            moduleAssignErrorLabel.setText("Please fill out all fields and select a lecturer.");
            return;
        }

        String username = selectedLecturer.split(":")[0]; // Extract username from the selected string

        try (Connection connection = DatabaseConnector.connect()) {
            String query = "INSERT INTO lecturer_modules (lecturer_username, module_name, class_name, semester, academic_year) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, moduleName);
            stmt.setString(3, className);
            stmt.setString(4, semester);
            stmt.setString(5, academicYear);
            stmt.executeUpdate();

            clearModuleFields();
            moduleAssignErrorLabel.setText("Module assigned successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            moduleAssignErrorLabel.setText("Error assigning module: " + e.getMessage());
        }
    }

    private void clearModuleFields() {
        moduleAssignField.clear();
        classAssignField.clear();
        semesterAssignField.clear();
        academicYearAssignField.clear();
    }

    @FXML
    private void addStudent() {
        String name = studentNameField.getText();
        String surname = studentSurnameField.getText();
        String studentNumber = studentNumberField.getText();
        String email = studentEmailField.getText();
        String contacts = studentContactsField.getText();

        if (name.isEmpty() || surname.isEmpty() || studentNumber.isEmpty() || email.isEmpty() || contacts.isEmpty()) {
            addStudentErrorLabel.setText("All fields must be filled out.");
            return;
        }

        try (Connection connection = DatabaseConnector.connect()) {
            String query = "INSERT INTO student (StudentName, StudentSurname, StudentNumber, Email, Contacts) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, surname);
            stmt.setString(3, studentNumber);
            stmt.setString(4, email);
            stmt.setString(5, contacts);
            stmt.executeUpdate();

            clearStudentFields();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Student Added");
            alert.setContentText("Student has been successfully added.");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
            addStudentErrorLabel.setText("Error adding student: " + e.getMessage());
        }
    }

    private void clearStudentFields() {
        studentNameField.clear();
        studentSurnameField.clear();
        studentNumberField.clear();
        studentEmailField.clear();
        studentContactsField.clear();
    }

    @FXML
    private void viewAllStudents() {
        StringBuilder content = new StringBuilder();
        try (Connection connection = DatabaseConnector.connect()) {
            String query = "SELECT StudentName, StudentSurname, StudentNumber, Email FROM student";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String studentInfo = String.format("Name: %s %s, Student Number: %s, Email: %s\n",
                        rs.getString("StudentName"),
                        rs.getString("StudentSurname"),
                        rs.getString("StudentNumber"),
                        rs.getString("Email"));
                content.append(studentInfo);
            }

            if (content.length() == 0) {
                content.append("No students found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            content.append("Error loading students: ").append(e.getMessage());
        }

        studentsListLabel.setText(content.toString());
    }

    @FXML
    private void addAcademicYear() {
        String academicYear = academicYearField.getText();
        if (academicYear.isEmpty()) {
            addErrorLabel.setText("Academic Year cannot be empty.");
            return;
        }

        try (Connection connection = DatabaseConnector.connect()) {
            String query = "INSERT INTO academic_years (year) VALUES (?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, academicYear);
            stmt.executeUpdate();
            academicYearField.clear();
        } catch (SQLException e) {
            e.printStackTrace();
            addErrorLabel.setText("Error adding academic year: " + e.getMessage());
        }
    }

    @FXML
    private void addSemester() {
        String semester = semesterField.getText();
        if (semester.isEmpty()) {
            addErrorLabel.setText("Semester cannot be empty.");
            return;
        }

        try (Connection connection = DatabaseConnector.connect()) {
            String query = "INSERT INTO semesters (semester_name) VALUES (?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, semester);
            stmt.executeUpdate();
            semesterField.clear();
        } catch (SQLException e) {
            e.printStackTrace();
            addErrorLabel.setText("Error adding semester: " + e.getMessage());
        }
    }

    @FXML
    private void addModule() {
        String module = moduleField.getText();
        if (module.isEmpty()) {
            addErrorLabel.setText("Module cannot be empty.");
            return;
        }

        try (Connection connection = DatabaseConnector.connect()) {
            String query = "INSERT INTO modules (module_name) VALUES (?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, module);
            stmt.executeUpdate();
            moduleField.clear();
        } catch (SQLException e) {
            e.printStackTrace();
            addErrorLabel.setText("Error adding module: " + e.getMessage());
        }
    }

    @FXML
    private void addClass() {
        String className = classField.getText();
        if (className.isEmpty()) {
            addErrorLabel.setText("Class cannot be empty.");
            return;
        }

        try (Connection connection = DatabaseConnector.connect()) {
            String query = "INSERT INTO classes (class_name) VALUES (?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, className);
            stmt.executeUpdate();
            classField.clear();
        } catch (SQLException e) {
            e.printStackTrace();
            addErrorLabel.setText("Error adding class: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) lecturerNameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Home");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            addErrorLabel.setText("Error loading Home page: " + e.getMessage());
        }
    }

    @FXML
    private void viewAllLecturers() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("All Lecturers");
        alert.setHeaderText("List of Lecturers");

        StringBuilder content = new StringBuilder();
        for (String lecturer : lecturersListView.getItems()) {
            content.append(lecturer).append("\n");
        }

        alert.setContentText(content.toString());
        alert.showAndWait();
    }

    @FXML
    private void viewAllModules() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("All Modules");
        alert.setHeaderText("List of Modules");

        StringBuilder content = new StringBuilder();
        try (Connection connection = DatabaseConnector.connect()) {
            String query = "SELECT module_name FROM modules";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                content.append(rs.getString("module_name")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            content.append("Error loading modules: ").append(e.getMessage());
        }

        alert.setContentText(content.toString());
        alert.showAndWait();
    }

    @FXML
    private void viewAllSemesters() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("All Semesters");
        alert.setHeaderText("List of Semesters");

        StringBuilder content = new StringBuilder();
        try (Connection connection = DatabaseConnector.connect()) {
            String query = "SELECT semester_name FROM semesters";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                content.append(rs.getString("semester_name")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            content.append("Error loading semesters: ").append(e.getMessage());
        }

        alert.setContentText(content.toString());
        alert.showAndWait();
    }
}