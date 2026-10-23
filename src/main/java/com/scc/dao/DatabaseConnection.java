package com.scc.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    // Match DatabaseInitializer's DB name
    private static final String DB_URL = "jdbc:sqlite:condominium.db";

    public static Connection connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(DB_URL);
        } catch (Exception e) {
            System.out.println("[✗] DB Connection Error: " + e.getMessage());
            return null;
        }
    }
}