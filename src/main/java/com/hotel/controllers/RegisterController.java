package com.hotel.controllers;

import com.hotel.app.SceneNavigator;
import com.hotel.database.HotelDatabase;
import com.hotel.models.enums.Gender;
import com.hotel.models.rooms.RoomType;
import com.hotel.models.users.Guest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * Controller for Register.fxml.
 * Validates input and creates a new Guest account.
 */
public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField addressField;
    @FXML private DatePicker dobPicker;
    @FXML private ComboBox<String> genderCombo;
    @FXML private ComboBox<String> preferenceCombo;
    @FXML private Label statusLabel;

    @FXML
    public void initialize() {
        genderCombo.getItems().addAll("MALE", "FEMALE");
        preferenceCombo.getItems().addAll("SINGLE", "DOUBLE", "SUITE");
    }

    @FXML
    public void handleRegister(ActionEvent event) {
        String username   = usernameField.getText().trim();
        String password   = passwordField.getText().trim();
        String confirmPwd = confirmPasswordField.getText().trim();
        String address    = addressField.getText().trim();
        LocalDate dob     = dobPicker.getValue();
        String genderStr  = genderCombo.getValue();
        String prefStr    = preferenceCombo.getValue();

        // --- Input Validation ---
        if (username.isEmpty() || password.isEmpty() || address.isEmpty()
                || dob == null || genderStr == null || prefStr == null) {
            showStatus("All fields are required.", true);
            return;
        }
        if (!password.equals(confirmPwd)) {
            showStatus("Passwords do not match.", true);
            return;
        }
        if (username.contains(" ")) {
            showStatus("Username cannot contain spaces.", true);
            return;
        }
        if (HotelDatabase.findGuest(username) != null) {
            showStatus("Username already exists. Choose another.", true);
            return;
        }

        try {
            Gender gender  = Gender.valueOf(genderStr);
            RoomType.TypeName pref = RoomType.TypeName.valueOf(prefStr);
            Guest newGuest = new Guest(username, password, dob, address, gender, pref);
            HotelDatabase.getGuests().add(newGuest);
            showStatus("Account created! You can now log in.", false);
        } catch (Exception e) {
            showStatus("Error: " + e.getMessage(), true);
        }
    }

    @FXML
    public void handleBack(ActionEvent event) {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        SceneNavigator.navigateTo(stage, SceneNavigator.LOGIN);
    }

    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setStyle(isError ? "-fx-text-fill: #e74c3c;" : "-fx-text-fill: #27ae60;");
    }
}
