package com.enyawevia.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionService {

    public Connection establishConnection( ) {

        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/michelle-ers";
            String username = "postgres";
            String password = "Code2021";
            Connection connect = DriverManager.getConnection(url, username, password);

            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("failed to connect to database");
            e.printStackTrace();
            return null;
        }
    }



}
