package com.hotel.network;

import java.io.*;
import java.net.*;
import java.util.function.Consumer;

/**
 * Client-side socket connection to the ChatServer.
 * Runs a background reader thread to receive incoming messages.
 * Passes received messages to a callback so the JavaFX controller
 * can display them using Platform.runLater().
 */
public class ChatClient implements Runnable {

    private final String host;
    private final int port;
    private final String username;
    private final Consumer<String> onMessageReceived;

    private Socket socket;
    private PrintWriter out;

    public ChatClient(String host, int port, String username, Consumer<String> onMessageReceived)
            throws IOException {
        this.host              = host;
        this.port              = port;
        this.username          = username;
        this.onMessageReceived = onMessageReceived;

        // Establish connection immediately at construction
        socket = new Socket(host, port);
        out    = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

        // First thing we send is our username so the server knows who we are
        out.println(username);
    }

    /**
     * Continuously reads messages from the server.
     * This runs on a background daemon thread — never call UI updates here directly.
     */
    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                onMessageReceived.accept(line);
            }
        } catch (IOException e) {
            onMessageReceived.accept("[System] Disconnected from chat server.");
        }
    }

    /**
     * Sends a message to the server. Safe to call from the JavaFX thread.
     */
    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public void disconnect() {
        try {
            if (socket != null) socket.close();
        } catch (IOException ignored) {}
    }
}
