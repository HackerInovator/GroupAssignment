package com.example.wings2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {

    public void insertReportEntry(String lecturer, String className, String courseName,
                                  String chapterCovered, String modeOfDelivery,
                                  int missedClasses, int attendance,
                                  int registrations, String challenges,
                                  String recommendations, String malpractice) throws SQLException {
        String query = "INSERT INTO prl_report (lecturer, class_name, course_name, chapter_covered, " +
                "mode_of_delivery, missed_classes, attendance, registrations, challenges, " +
                "recommendations, malpractice) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, lecturer);
            stmt.setString(2, className);
            stmt.setString(3, courseName);
            stmt.setString(4, chapterCovered);
            stmt.setString(5, modeOfDelivery);
            stmt.setInt(6, missedClasses);
            stmt.setInt(7, attendance);
            stmt.setInt(8, registrations);
            stmt.setString(9, challenges);
            stmt.setString(10, recommendations);
            stmt.setString(11, malpractice);
            stmt.executeUpdate();
        }
    }

    public List<ReportEntry> getAllReportEntries() throws SQLException {
        List<ReportEntry> entries = new ArrayList<>();
        String query = "SELECT lecturer, class_name, course_name, chapter_covered, mode_of_delivery, " +
                "missed_classes, attendance, registrations, challenges, recommendations, malpractice " +
                "FROM prl_report";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ReportEntry entry = new ReportEntry(
                        rs.getString("lecturer"),
                        rs.getString("class_name"),
                        rs.getString("course_name"),
                        rs.getString("chapter_covered"),
                        rs.getString("mode_of_delivery"),
                        rs.getInt("missed_classes"),
                        rs.getInt("attendance"),
                        rs.getInt("registrations"),
                        rs.getString("challenges"),
                        rs.getString("recommendations"),
                        rs.getString("malpractice")
                );
                entries.add(entry);
            }
        }
        return entries;
    }
}