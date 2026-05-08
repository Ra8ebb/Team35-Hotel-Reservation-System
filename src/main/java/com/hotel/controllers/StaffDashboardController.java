package com.hotel.controllers;

import com.hotel.app.SceneNavigator;
import com.hotel.app.SessionManager;
import com.hotel.database.HotelDatabase;
import com.hotel.models.bookings.Reservation;
import com.hotel.models.enums.ReservationStatus;
import com.hotel.models.rooms.Room;
import com.hotel.models.users.Guest;
import com.hotel.models.users.Staff;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * Controller for StaffDashboard.fxml.
 * Provides staff with an overview of all guests, rooms, and reservations.
 */
public class StaffDashboardController {

    @FXML private Label welcomeLabel;
    @FXML private Label roleLabel;
    @FXML private ListView<String> guestList;
    @FXML private ListView<String> roomList;
    @FXML private ListView<String> reservationList;

    @FXML
    public void initialize() {
        Staff staff = SessionManager.getInstance().getLoggedInStaff();
        if (staff == null) return;

        welcomeLabel.setText("Welcome, " + staff.getUsername());
        roleLabel.setText("Role: " + staff.getRole());

        loadGuests();
        loadRooms();
        loadReservations();
    }

    private void loadGuests() {
        guestList.getItems().clear();
        for (Guest g : HotelDatabase.getGuests()) {
            guestList.getItems().add(g.getUsername() + " | " + g.getGender() + " | " + g.getAddress());
        }
        if (guestList.getItems().isEmpty()) guestList.getItems().add("No guests registered.");
    }

    private void loadRooms() {
        roomList.getItems().clear();
        for (Room r : HotelDatabase.getRooms()) {
            String entry = "Room " + r.getRoomNo()
                    + " | " + r.getType().getName()
                    + " | $" + r.getType().getPricePerNight()
                    + " | " + (r.isAvailable() ? "Available" : "Occupied");
            roomList.getItems().add(entry);
        }
    }

    private void loadReservations() {
        reservationList.getItems().clear();
        for (Reservation res : HotelDatabase.getReservations()) {
            if (res.getStatus() != ReservationStatus.CANCELLED) {
                String entry = res.getGuest().getUsername()
                        + " → Room " + res.getRoom().getRoomNo()
                        + " | " + res.getCheckInDate() + " to " + res.getCheckOutDate()
                        + " | " + res.getStatus();
                reservationList.getItems().add(entry);
            }
        }
        if (reservationList.getItems().isEmpty()) reservationList.getItems().add("No active reservations.");
    }

    @FXML public void goToChat() {
        navigate(SceneNavigator.CHAT);
    }

    @FXML public void handleLogout() {
        SessionManager.getInstance().logout();
        navigate(SceneNavigator.LOGIN);
    }

    private void navigate(String path) {
        Stage stage = (Stage) welcomeLabel.getScene().getWindow();
        SceneNavigator.navigateTo(stage, path);
    }
}
