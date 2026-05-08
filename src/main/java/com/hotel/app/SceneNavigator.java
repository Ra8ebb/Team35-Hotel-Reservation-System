package com.hotel.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Utility class for switching JavaFX scenes.
 * Controllers call SceneNavigator.navigateTo(...) instead of duplicating scene-switching code.
 */
public class SceneNavigator {

    // Paths to FXML files (relative to resources root)
    public static final String LOGIN       = "/com/hotel/views/Login.fxml";
    public static final String REGISTER    = "/com/hotel/views/Register.fxml";
    public static final String GUEST_DASH  = "/com/hotel/views/GuestDashboard.fxml";
    public static final String ROOMS       = "/com/hotel/views/RoomBrowse.fxml";
    public static final String RESERVATIONS= "/com/hotel/views/Reservations.fxml";
    public static final String PAYMENT     = "/com/hotel/views/Payment.fxml";
    public static final String CHAT        = "/com/hotel/views/Chat.fxml";
    public static final String STAFF_DASH  = "/com/hotel/views/StaffDashboard.fxml";

    /**
     * Replaces the current scene on the given stage with the requested FXML screen.
     */
    public static void navigateTo(Stage stage, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(SceneNavigator.class.getResource(fxmlPath));
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(
                SceneNavigator.class.getResource("/css/hotel.css").toExternalForm()
            );
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("[Navigator] Failed to load screen: " + fxmlPath);
            e.printStackTrace();
        }
    }
}
