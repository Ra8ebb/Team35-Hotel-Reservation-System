package com.hotel.controllers;

import com.hotel.app.SceneNavigator;
import com.hotel.database.HotelDatabase;
import com.hotel.models.rooms.Amenity;
import com.hotel.models.rooms.Room;
import com.hotel.threads.RoomAvailabilityService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Controller for RoomBrowse.fxml.
 * Displays available rooms and supports filtering by type, price, and amenity.
 * Uses a background thread (RoomAvailabilityService) to periodically refresh the list.
 */
public class RoomBrowseController {

    @FXML private ComboBox<String> typeFilter;
    @FXML private TextField maxPriceField;
    @FXML private TextField amenityFilter;
    @FXML private ListView<String> roomListView;
    @FXML private Label statusLabel;

    private RoomAvailabilityService availabilityService;

    @FXML
    public void initialize() {
        typeFilter.getItems().addAll("ALL", "SINGLE", "DOUBLE", "SUITE");
        typeFilter.setValue("ALL");

        loadRooms();
        startBackgroundRefresh();
    }

    /** Populates the room list based on current filter values. */
    @FXML
    public void applyFilters() {
        loadRooms();
    }

    private void loadRooms() {
        roomListView.getItems().clear();
        String selectedType = typeFilter.getValue();
        String priceText    = maxPriceField.getText().trim();
        String amenityText  = amenityFilter.getText().trim().toLowerCase();

        double maxPrice = Double.MAX_VALUE;
        if (!priceText.isEmpty()) {
            try { maxPrice = Double.parseDouble(priceText); }
            catch (NumberFormatException e) { statusLabel.setText("Invalid price value."); return; }
        }

        ArrayList<Room> rooms = HotelDatabase.getRooms();
        int shown = 0;
        for (Room room : rooms) {
            if (!room.isAvailable()) continue;

            // Type filter
            if (!selectedType.equals("ALL") &&
                !room.getType().getName().toString().equals(selectedType)) continue;

            // Price filter
            if (room.getType().getPricePerNight() > maxPrice) continue;

            // Amenity filter
            if (!amenityText.isEmpty()) {
                boolean hasAmenity = false;
                for (Amenity a : room.getAmenities()) {
                    if (a.getName().toLowerCase().contains(amenityText)) { hasAmenity = true; break; }
                }
                if (!hasAmenity) continue;
            }

            String entry = "Room " + room.getRoomNo()
                    + " | Floor " + room.getFloorNo()
                    + " | " + room.getType().getName()
                    + " | $" + room.getType().getPricePerNight() + "/night"
                    + " | Amenities: " + formatAmenities(room);
            roomListView.getItems().add(entry);
            shown++;
        }
        statusLabel.setText(shown + " room(s) found.");
    }

    private String formatAmenities(Room room) {
        if (room.getAmenities().isEmpty()) return "None";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < room.getAmenities().size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(room.getAmenities().get(i).getName());
        }
        return sb.toString();
    }

    /**
     * Starts a background thread that refreshes the room list every 10 seconds.
     * Uses Platform.runLater() to safely update the JavaFX UI from the background thread.
     */
    private void startBackgroundRefresh() {
        availabilityService = new RoomAvailabilityService(10000, () -> {
            Platform.runLater(() -> {
                loadRooms();
                statusLabel.setText("Rooms refreshed automatically.");
            });
        });
        Thread t = new Thread(availabilityService);
        t.setDaemon(true);
        t.start();
    }

    @FXML public void goBack() {
        Stage stage = (Stage) roomListView.getScene().getWindow();
        SceneNavigator.navigateTo(stage, SceneNavigator.GUEST_DASH);
    }

    @FXML public void goToReservations() {
        Stage stage = (Stage) roomListView.getScene().getWindow();
        SceneNavigator.navigateTo(stage, SceneNavigator.RESERVATIONS);
    }
}
