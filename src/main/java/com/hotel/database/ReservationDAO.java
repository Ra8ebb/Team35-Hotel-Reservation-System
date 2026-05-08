package com.hotel.database;

import java.sql.*;
import java.time.LocalDate;

/**
 * Data Access Object for Reservation persistence.
 * Handles saving and loading reservations to/from SQLite.
 */
public class ReservationDAO {

    /** Inserts a new reservation record and returns the generated ID, or -1 on failure. */
    public static int insertReservation(String guestUsername, int roomNo,
                                        LocalDate checkIn, LocalDate checkOut,
                                        String status, double totalCost) {
        String sql = "INSERT INTO reservations (guestUsername, roomNo, checkInDate, checkOutDate, status, totalCost) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, guestUsername);
            ps.setInt(2, roomNo);
            ps.setString(3, checkIn.toString());
            ps.setString(4, checkOut.toString());
            ps.setString(5, status);
            ps.setDouble(6, totalCost);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
        } catch (SQLException e) {
            System.err.println("[DAO] Insert reservation failed: " + e.getMessage());
        }
        return -1;
    }

    /** Updates the status field of an existing reservation by its database ID. */
    public static void updateStatus(int reservationId, String status) {
        String sql = "UPDATE reservations SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, reservationId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DAO] Update status failed: " + e.getMessage());
        }
    }

    /** Saves a payment record linked to a reservation. */
    public static void insertPayment(int reservationId, double amount,
                                     String paymentMethod, LocalDate date) {
        String sql = "INSERT INTO payments (reservationId, amount, paymentMethod, paymentDate) VALUES (?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reservationId);
            ps.setDouble(2, amount);
            ps.setString(3, paymentMethod);
            ps.setString(4, date.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DAO] Insert payment failed: " + e.getMessage());
        }
    }

    /** Saves a chat message to the database. */
    public static void insertChatMessage(String sender, String message) {
        String sql = "INSERT INTO chat_messages (sender, message, timestamp) VALUES (?,?,?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sender);
            ps.setString(2, message);
            ps.setString(3, LocalDate.now().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DAO] Insert chat message failed: " + e.getMessage());
        }
    }
}
