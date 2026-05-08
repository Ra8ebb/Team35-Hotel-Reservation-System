package com.hotel.controllers;

import com.hotel.app.SceneNavigator;
import com.hotel.app.SessionManager;
import com.hotel.database.HotelDatabase;
import com.hotel.models.bookings.Reservation;
import com.hotel.models.enums.ReservationStatus;
import com.hotel.models.users.Guest;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * Controller for GuestDashboard.fxml.
 * Displays the logged-in guest's profile information and active reservations.
 */
public class GuestDashboardController {

    @FXML private Label welcomeLabel;
    @FXML private Label usernameLabel;
    @FXML private Label dobLabel;
    @FXML private Label addressLabel;
    @FXML private Label genderLabel;
    @FXML private Label preferenceLabel;
    @FXML private Label balanceLabel;
    @FXML private ListView<String> reservationsList;

    @FXML
    public void initialize() {
        Guest guest = SessionManager.getInstance().getLoggedInGuest();
        if (guest == null) return;

        welcomeLabel.setText("Welcome, " + guest.getUsername() + "!");
        usernameLabel.setText(guest.getUsername());
        dobLabel.setText(guest.getDateOfBirth().toString());
        addressLabel.setText(guest.getAddress());
        genderLabel.setText(guest.getGender().toString());
        preferenceLabel.setText(guest.getRoomPreference().toString());
        balanceLabel.setText("$1,000.00"); // Default balance for demo

        loadReservations(guest);
    }

    private void loadReservations(Guest guest) {
        reservationsList.getItems().clear();
        for (Reservation r : HotelDatabase.getReservations()) {
            if (r.getGuest().getUsername().equals(guest.getUsername())
                    && r.getStatus() != ReservationStatus.CANCELLED) {
                String entry = "Room " + r.getRoom().getRoomNo()
                        + " | " + r.getCheckInDate() + " → " + r.getCheckOutDate()
                        + " | " + r.getStatus();
                reservationsList.getItems().add(entry);
            }
        }
        if (reservationsList.getItems().isEmpty()) {
            reservationsList.getItems().add("No active reservations.");
        }
    }

    @FXML public void goToRooms()         { navigate(SceneNavigator.ROOMS); }
    @FXML public void goToReservations()  { navigate(SceneNavigator.RESERVATIONS); }
    @FXML public void goToPayment()       { navigate(SceneNavigator.PAYMENT); }
    @FXML public void goToChat()          { navigate(SceneNavigator.CHAT); }

    @FXML
    public void handleLogout() {
        SessionManager.getInstance().logout();
        navigate(SceneNavigator.LOGIN);
    }

    private void navigate(String path) {
        Stage stage = (Stage) welcomeLabel.getScene().getWindow();
        SceneNavigator.navigateTo(stage, path);
    }
}
