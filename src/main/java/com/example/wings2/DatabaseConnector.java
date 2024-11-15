package com.example.wings2;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnector {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/academic_reporting";
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASSWORD = "2003v400"; // Replace with your MySQL password
    private static final Logger LOGGER = Logger.getLogger(DatabaseConnector.class.getName());

    public static Connection connect() {
        Connection connection = null;
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            LOGGER.info("Connection established!");

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM USERS");

            while (resultSet.next()){
                System.out.println(resultSet.getString("Username"));
                System.out.println(resultSet.getString("Password"));
            }

        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found.", e);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Connection failed!", e);
        }
        return connection;
    }
}



