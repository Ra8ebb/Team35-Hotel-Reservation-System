package com.hotel.network;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Simple multi-client chat server using Java Sockets.
 * Each connected client gets its own ClientHandler thread.
 * Run this class separately (as its own main) before launching the hotel app.
 *
 * Usage: java com.hotel.network.ChatServer
 */
public class ChatServer {

    private static final int PORT = 9090;

    // Thread-safe list of all active client writers
    private static final List<PrintWriter> clients =
            Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        System.out.println("[Server] Chat server starting on port " + PORT + "...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[Server] Listening for connections...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[Server] New client connected: " + clientSocket.getInetAddress());
                ClientHandler handler = new ClientHandler(clientSocket, clients);
                Thread t = new Thread(handler);
                t.setDaemon(true);
                t.start();
            }
        } catch (IOException e) {
            System.err.println("[Server] Server error: " + e.getMessage());
        }
    }

    /**
     * Broadcasts a message to every connected client.
     */
    public static void broadcast(String message, PrintWriter sender) {
        synchronized (clients) {
            for (PrintWriter writer : clients) {
                // FIX: skip the sender so they don't receive their own message echoed back
                if (writer != sender) {
                    writer.println(message);
                }
            }
        }
    }

    /**
     * Handles a single connected client in its own thread.
     */
    static class ClientHandler implements Runnable {
        private final Socket socket;
        private final List<PrintWriter> clients;
        private PrintWriter out;
        private String clientName = "Unknown";

        ClientHandler(Socket socket, List<PrintWriter> clients) {
            this.socket  = socket;
            this.clients = clients;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                clients.add(out);

                // First message from client is the username
                clientName = in.readLine();
                ChatServer.broadcast("[Server] " + clientName + " has joined the chat.", out);

                String line;
                while ((line = in.readLine()) != null) {
                    ChatServer.broadcast(clientName + ": " + line, out);
                }
            } catch (IOException e) {
                System.out.println("[Server] Client disconnected: " + clientName);
            } finally {
                if (out != null) clients.remove(out);
                ChatServer.broadcast("[Server] " + clientName + " has left the chat.", null);
                try { socket.close(); } catch (IOException ignored) {}
            }
        }
    }
}
