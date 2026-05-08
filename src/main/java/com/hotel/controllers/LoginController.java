package com.hotel.controllers;

import com.hotel.app.SceneNavigator;
import com.hotel.app.SessionManager;
import com.hotel.database.HotelDatabase;
import com.hotel.models.users.Guest;
import com.hotel.models.users.Staff;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Controller for Login.fxml.
 * Handles both Guest and Staff login flows.
 */
public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ToggleGroup loginTypeGroup;
    @FXML private RadioButton guestRadio;
    @FXML private RadioButton staffRadio;
    @FXML private Label statusLabel;

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showStatus("Please enter both username and password.", true);
            return;
        }

        Stage stage = (Stage) usernameField.getScene().getWindow();

        if (guestRadio.isSelected()) {
            Guest guest = HotelDatabase.findGuest(username);
            if (guest != null && guest.getPassword().equals(password)) {
                SessionManager.getInstance().setLoggedInGuest(guest);
                SceneNavigator.navigateTo(stage, SceneNavigator.GUEST_DASH);
            } else {
                showStatus("Invalid guest credentials. Try Mario / 1234", true);
            }
        } else {
            Staff staff = HotelDatabase.findStaff(username);
            if (staff != null && staff.getPassword().equals(password)) {
                SessionManager.getInstance().setLoggedInStaff(staff);
                SceneNavigator.navigateTo(stage, SceneNavigator.STAFF_DASH);
            } else {
                showStatus("Invalid staff credentials. Try admin / admin123", true);
            }
        }
    }

    @FXML
    public void handleRegister(ActionEvent event) {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        SceneNavigator.navigateTo(stage, SceneNavigator.REGISTER);
    }

    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setStyle(isError ? "-fx-text-fill: #e74c3c;" : "-fx-text-fill: #27ae60;");
    }
}
