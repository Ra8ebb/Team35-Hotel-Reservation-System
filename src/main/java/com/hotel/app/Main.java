package com.hotel.app;

import com.hotel.database.DatabaseManager;
import com.hotel.database.HotelDatabase;
import com.hotel.threads.CSVExportService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main entry point for the Hotel Reservation System (Milestone 2).
 * Boots the database, seeds dummy data, starts background services,
 * then launches the JavaFX GUI.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the Login screen as the first scene
        Parent root = FXMLLoader.load(getClass().getResource("/com/hotel/views/Login.fxml"));
        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(getClass().getResource("/css/hotel.css").toExternalForm());

        primaryStage.setTitle("Hotel Reservation System");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(
                new javafx.scene.image.Image(
                        getClass().getResourceAsStream("/images/hotel.png")
                )
        );
       // primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // 1. Initialize SQLite database tables
        DatabaseManager.initializeDatabase();

        // 2. Seed dummy in-memory data
        HotelDatabase.initializeDummyData();

        // 3. Start background CSV export thread (daemon so it dies with the app)
        CSVExportService exportTask = new CSVExportService(30000);
        Thread exportThread = new Thread(exportTask);
        exportThread.setDaemon(true);
        exportThread.start();

        // 4. Launch JavaFX UI
        launch(args);
    }
}
