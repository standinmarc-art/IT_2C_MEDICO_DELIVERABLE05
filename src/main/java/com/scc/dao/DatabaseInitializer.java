package com.scc.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    private static final String DB_URL = "jdbc:sqlite:condominium.db";

    public static Connection getConnection() throws SQLException {
        return DatabaseConnection.connect();
    }

    public static void init() {
        String createUsersTable = """
            CREATE TABLE IF NOT EXISTS Users (
                User_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                Username TEXT NOT NULL UNIQUE
                    CHECK (length(trim(Username)) > 0),
                Password TEXT NOT NULL
                    CHECK (length(trim(Password)) > 0),
                Role TEXT NOT NULL
                    CHECK (Role IN ('ADMIN', 'SECURITY', 'DELIVERY')),
                AccountStatus TEXT NOT NULL DEFAULT 'PENDING'
                    CHECK (AccountStatus IN ('PENDING', 'APPROVED', 'SUSPENDED'))
            )
            """;

        String createRolesTable = """
            CREATE TABLE IF NOT EXISTS Roles (
                Role_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                RoleName TEXT NOT NULL UNIQUE,
                Description TEXT NOT NULL
            )
            """;

        String insertRoles = """
            INSERT OR IGNORE INTO Roles (RoleName, Description) VALUES
            ('ADMIN', 'Condo Administrator / Building Management'),
            ('SECURITY', 'Security Gate Staff / Guards'),
            ('DELIVERY', 'Service / Delivery Personnel')
            """;

        String createRecordsTable = """
            CREATE TABLE IF NOT EXISTS Records (
                Record_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                Username TEXT NOT NULL,
                Description TEXT NOT NULL
                    CHECK (length(trim(Description)) > 0),
                RecordDate TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (Username) REFERENCES Users(Username) ON DELETE CASCADE
            )
            """;

        // DEFAULT ADMIN — will NOT overwrite if already exists
        String insertDefaultAdmin = """
            INSERT OR IGNORE INTO Users (Username, Password, Role, AccountStatus)
            VALUES ('admin', 'admin123', 'ADMIN', 'APPROVED')
            """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("PRAGMA foreign_keys = ON");
            stmt.execute(createUsersTable);
            stmt.execute(createRolesTable);
            stmt.executeUpdate(insertRoles);
            stmt.execute(createRecordsTable);

            // This line creates the default Admin
            stmt.executeUpdate(insertDefaultAdmin);

            System.out.println("[SUCCESS] Database initialized — Default Admin ready.");
            System.out.println("  Admin Login → Username: admin | Password: admin123");

        } catch (SQLException e) {
            System.err.println("[ERROR] DB init failed: " + e.getMessage());
            throw new RuntimeException("DB initialization error", e);
        }
    }

    public static void main(String[] args) {
        init();
    }
}