package com.hotel.controllers;

import com.hotel.app.SceneNavigator;
import com.hotel.app.SessionManager;
import com.hotel.database.HotelDatabase;
import com.hotel.database.ReservationDAO;
import com.hotel.models.bookings.Reservation;
import com.hotel.models.enums.ReservationStatus;
import com.hotel.models.rooms.Room;
import com.hotel.models.users.Guest;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Controller for Reservations.fxml.
 * Allows guests to create new reservations, view history, and cancel bookings.
 */
public class ReservationsController {

    // --- Create Reservation Section ---
    @FXML private ComboBox<String> roomCombo;
    @FXML private DatePicker checkInPicker;
    @FXML private DatePicker checkOutPicker;
    @FXML private Label costPreviewLabel;
    @FXML private Label createStatusLabel;

    // --- Reservation History Section ---
    @FXML private ListView<String> historyList;
    @FXML private Label cancelStatusLabel;

    @FXML
    public void initialize() {
        loadAvailableRooms();
        loadHistory();
        checkInPicker.setValue(LocalDate.now());
        checkOutPicker.setValue(LocalDate.now().plusDays(1));
        updateCostPreview();
    }

    private void loadAvailableRooms() {
        roomCombo.getItems().clear();
        for (Room r : HotelDatabase.getRooms()) {
            if (r.isAvailable()) {
                roomCombo.getItems().add("Room " + r.getRoomNo()
                        + " - " + r.getType().getName()
                        + " ($" + r.getType().getPricePerNight() + "/night)");
            }
        }
        if (roomCombo.getItems().isEmpty()) {
            roomCombo.getItems().add("No rooms available");
        }
    }

    @FXML
    public void updateCostPreview() {
        LocalDate in  = checkInPicker.getValue();
        LocalDate out = checkOutPicker.getValue();
        String selected = roomCombo.getValue();
        if (in == null || out == null || selected == null || !out.isAfter(in)) {
            costPreviewLabel.setText("Select valid dates to see cost.");
            return;
        }
        long nights = ChronoUnit.DAYS.between(in, out);
        double pricePerNight = getPriceFromCombo(selected);
        costPreviewLabel.setText("Estimated cost: $" + String.format("%.2f", nights * pricePerNight));
    }

    private double getPriceFromCombo(String comboValue) {
        // Parse the room number from the combo string to look up the room
        try {
            String[] parts = comboValue.split(" ");
            int roomNo = Integer.parseInt(parts[1]);
            for (Room r : HotelDatabase.getRooms()) {
                if (r.getRoomNo() == roomNo) return r.getType().getPricePerNight();
            }
        } catch (Exception ignored) {}
        return 0.0;
    }

    @FXML
    public void handleCreateReservation() {
        Guest guest     = SessionManager.getInstance().getLoggedInGuest();
        String selected = roomCombo.getValue();
        LocalDate in    = checkInPicker.getValue();
        LocalDate out   = checkOutPicker.getValue();

        if (guest == null) { showCreate("You must be logged in as a guest.", true); return; }
        if (selected == null || in == null || out == null) {
            showCreate("Please fill in all fields.", true); return;
        }
        if (!out.isAfter(in)) {
            showCreate("Check-out must be after check-in.", true); return;
        }
        if (in.isBefore(LocalDate.now())) {
            showCreate("Check-in cannot be in the past.", true); return;
        }

        try {
            String[] parts = selected.split(" ");
            int roomNo = Integer.parseInt(parts[1]);
            Room room = null;
            for (Room r : HotelDatabase.getRooms()) {
                if (r.getRoomNo() == roomNo) { room = r; break; }
            }
            if (room == null || !room.isAvailable()) {
                showCreate("Selected room is no longer available.", true); return;
            }

            Reservation res = new Reservation(guest, room, in, out);
            res.setStatus(ReservationStatus.CONFIRMED);
            room.setAvailability(false);
            HotelDatabase.getReservations().add(res);

            long nights = ChronoUnit.DAYS.between(in, out);
            double total = nights * room.getType().getPricePerNight();
            // FIX: store the DB-generated id on the in-memory reservation
            int dbId = ReservationDAO.insertReservation(guest.getUsername(), roomNo, in, out, "CONFIRMED", total);
            res.setId(dbId);

            showCreate("Reservation created successfully!", false);
            loadAvailableRooms();
            loadHistory();
        } catch (Exception e) {
            showCreate("Error: " + e.getMessage(), true);
        }
    }

    private void loadHistory() {
        historyList.getItems().clear();
        Guest guest = SessionManager.getInstance().getLoggedInGuest();
        if (guest == null) return;

        for (Reservation r : HotelDatabase.getReservations()) {
            if (r.getGuest().getUsername().equals(guest.getUsername())) {
                String entry = "Room " + r.getRoom().getRoomNo()
                        + " | " + r.getCheckInDate() + " → " + r.getCheckOutDate()
                        + " | " + r.getStatus();
                historyList.getItems().add(entry);
            }
        }
        if (historyList.getItems().isEmpty()) {
            historyList.getItems().add("No reservation history found.");
        }
    }

    @FXML
    public void handleCancelSelected() {
        String selected = historyList.getSelectionModel().getSelectedItem();
        if (selected == null || selected.contains("No reservation")) {
            cancelStatusLabel.setText("Please select a reservation to cancel.");
            return;
        }

        Guest guest = SessionManager.getInstance().getLoggedInGuest();
        // Parse room number from the selected string
        try {
            String[] parts = selected.split(" ");
            int roomNo = Integer.parseInt(parts[1]);

            for (Reservation r : HotelDatabase.getReservations()) {
                if (r.getGuest().getUsername().equals(guest.getUsername())
                        && r.getRoom().getRoomNo() == roomNo
                        && r.getStatus() != ReservationStatus.CANCELLED) {
                    r.setStatus(ReservationStatus.CANCELLED);
                    r.getRoom().setAvailability(true);
                    cancelStatusLabel.setText("Reservation cancelled successfully.");
                    loadHistory();
                    loadAvailableRooms();
                    return;
                }
            }
            cancelStatusLabel.setText("Could not find an active reservation to cancel.");
        } catch (Exception e) {
            cancelStatusLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML public void goBack() {
        Stage stage = (Stage) historyList.getScene().getWindow();
        SceneNavigator.navigateTo(stage, SceneNavigator.GUEST_DASH);
    }

    private void showCreate(String msg, boolean isError) {
        createStatusLabel.setText(msg);
        createStatusLabel.setStyle(isError ? "-fx-text-fill:#e74c3c;" : "-fx-text-fill:#27ae60;");
    }
}
