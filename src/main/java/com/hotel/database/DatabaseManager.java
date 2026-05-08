package com.hotel.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages the SQLite connection and schema creation.
 * All tables needed for the application are created here on first run.
 */
public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:hotel_system.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Creates all required tables if they do not already exist.
     * Safe to call on every application start.
     */
    public static void initializeDatabase() {
        String guests = "CREATE TABLE IF NOT EXISTS guests ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT UNIQUE NOT NULL, "
                + "password TEXT NOT NULL, "
                + "dateOfBirth TEXT, "
                + "address TEXT, "
                + "gender TEXT, "
                + "roomPreference TEXT, "
                + "balance REAL DEFAULT 1000.0"
                + ");";

        String staff = "CREATE TABLE IF NOT EXISTS staff ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT UNIQUE NOT NULL, "
                + "password TEXT NOT NULL, "
                + "role TEXT NOT NULL"
                + ");";

        String rooms = "CREATE TABLE IF NOT EXISTS rooms ("
                + "roomNo INTEGER PRIMARY KEY, "
                + "floorNo INTEGER, "
                + "roomType TEXT, "
                + "pricePerNight REAL, "
                + "capacity INTEGER, "
                + "isAvailable INTEGER DEFAULT 1, "
                + "amenities TEXT"
                + ");";

        String reservations = "CREATE TABLE IF NOT EXISTS reservations ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "guestUsername TEXT NOT NULL, "
                + "roomNo INTEGER NOT NULL, "
                + "checkInDate TEXT, "
                + "checkOutDate TEXT, "
                + "status TEXT, "
                + "totalCost REAL"
                + ");";

        String payments = "CREATE TABLE IF NOT EXISTS payments ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "reservationId INTEGER, "
                + "amount REAL, "
                + "paymentMethod TEXT, "
                + "paymentDate TEXT"
                + ");";

        String chatMessages = "CREATE TABLE IF NOT EXISTS chat_messages ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "sender TEXT NOT NULL, "
                + "message TEXT NOT NULL, "
                + "timestamp TEXT"
                + ");";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(guests);
            stmt.execute(staff);
            stmt.execute(rooms);
            stmt.execute(reservations);
            stmt.execute(payments);
            stmt.execute(chatMessages);
            System.out.println("[Database] Schema initialized successfully.");
        } catch (SQLException e) {
            System.err.println("[Database] Initialization failed: " + e.getMessage());
        }
    }
}
