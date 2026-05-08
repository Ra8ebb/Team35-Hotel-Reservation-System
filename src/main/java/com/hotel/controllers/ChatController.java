package com.hotel.controllers;

import com.hotel.app.SceneNavigator;
import com.hotel.app.SessionManager;
import com.hotel.database.ReservationDAO;
import com.hotel.network.ChatClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Controller for Chat.fxml.
 * Connects to the ChatServer via a socket and allows real-time messaging.
 */
public class ChatController {

    @FXML private TextArea chatArea;
    @FXML private TextField messageField;
    @FXML private Label statusLabel;

    private ChatClient chatClient;

    @FXML
    public void initialize() {
        String username = resolveUsername();
        connectToServer(username);
    }

    private String resolveUsername() {
        if (SessionManager.getInstance().isGuestLoggedIn()) {
            return SessionManager.getInstance().getLoggedInGuest().getUsername();
        } else if (SessionManager.getInstance().isStaffLoggedIn()) {
            return SessionManager.getInstance().getLoggedInStaff().getUsername();
        }
        return "Anonymous";
    }

    private void connectToServer(String username) {
        try {
            chatClient = new ChatClient("localhost", 9090, username, message -> {
                // This callback is called from the background reader thread
                Platform.runLater(() -> chatArea.appendText(message + "\n"));
            });
            Thread clientThread = new Thread(chatClient);
            clientThread.setDaemon(true);
            clientThread.start();
            statusLabel.setText("Connected as: " + username);
        } catch (Exception e) {
            statusLabel.setText("Could not connect to chat server. Is it running?");
            chatArea.appendText("[System] Chat server offline. Start ChatServer first.\n");
        }
    }

    @FXML
    public void handleSend() {
        String text = messageField.getText().trim();
        if (text.isEmpty() || chatClient == null) return;

        // FIX: display own message locally since server no longer echoes back to sender
        String username = resolveUsername();
        chatArea.appendText(username + ": " + text + "\n");

        chatClient.sendMessage(text);
        ReservationDAO.insertChatMessage(username, text);
        messageField.clear();
    }

    @FXML
    public void handleEnterKey(javafx.scene.input.KeyEvent event) {
        if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
            handleSend();
        }
    }

    @FXML public void goBack() {
        Stage stage = (Stage) chatArea.getScene().getWindow();
        String target = SessionManager.getInstance().isGuestLoggedIn()
                ? SceneNavigator.GUEST_DASH : SceneNavigator.STAFF_DASH;
        SceneNavigator.navigateTo(stage, target);
    }
}
