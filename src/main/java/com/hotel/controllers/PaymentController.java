package com.hotel.controllers;

import com.hotel.app.SceneNavigator;
import com.hotel.app.SessionManager;
import com.hotel.database.HotelDatabase;
import com.hotel.database.ReservationDAO;
import com.hotel.models.bookings.Invoice;
import com.hotel.models.bookings.Reservation;
import com.hotel.models.enums.PaymentMethod;
import com.hotel.models.enums.ReservationStatus;
import com.hotel.models.users.Guest;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Controller for Payment.fxml.
 * Displays unpaid reservations, shows an invoice, and processes payment.
 */
public class PaymentController {

    @FXML private ComboBox<String> reservationCombo;
    @FXML private ComboBox<String> paymentMethodCombo;
    @FXML private TextArea invoiceArea;
    @FXML private Label statusLabel;

    @FXML
    public void initialize() {
        paymentMethodCombo.getItems().addAll("CASH", "CREDIT_CARD", "ONLINE");
        paymentMethodCombo.setValue("CASH");
        loadUnpaidReservations();
    }

    private void loadUnpaidReservations() {
        reservationCombo.getItems().clear();
        Guest guest = SessionManager.getInstance().getLoggedInGuest();
        if (guest == null) return;

        for (Reservation r : HotelDatabase.getReservations()) {
            if (r.getGuest().getUsername().equals(guest.getUsername())
                    && r.getStatus() == ReservationStatus.CONFIRMED) {
                String entry = "Room " + r.getRoom().getRoomNo()
                        + " | " + r.getCheckInDate() + " → " + r.getCheckOutDate();
                reservationCombo.getItems().add(entry);
            }
        }
        if (reservationCombo.getItems().isEmpty()) {
            reservationCombo.getItems().add("No confirmed reservations to pay.");
        }
    }

    @FXML
    public void generateInvoice() {
        String selected = reservationCombo.getValue();
        if (selected == null || selected.contains("No confirmed")) {
            invoiceArea.setText("No reservation selected.");
            return;
        }

        Reservation res = findSelectedReservation(selected);
        if (res == null) { invoiceArea.setText("Reservation not found."); return; }

        long nights = ChronoUnit.DAYS.between(res.getCheckInDate(), res.getCheckOutDate());
        double pricePerNight = res.getRoom().getType().getPricePerNight();
        double total = nights * pricePerNight;

        invoiceArea.setText(
            "========== INVOICE ==========\n" +
            "Guest    : " + res.getGuest().getUsername() + "\n" +
            "Room     : " + res.getRoom().getRoomNo() + " (" + res.getRoom().getType().getName() + ")\n" +
            "Check-In : " + res.getCheckInDate() + "\n" +
            "Check-Out: " + res.getCheckOutDate() + "\n" +
            "Nights   : " + nights + "\n" +
            "Rate     : $" + pricePerNight + " / night\n" +
            "-----------------------------\n" +
            "TOTAL    : $" + String.format("%.2f", total) + "\n" +
            "============================="
        );
    }

    @FXML
    public void handleConfirmPayment() {
        String selected = reservationCombo.getValue();
        String methodStr = paymentMethodCombo.getValue();

        if (selected == null || selected.contains("No confirmed")) {
            showStatus("No reservation selected.", true); return;
        }
        if (methodStr == null) {
            showStatus("Please select a payment method.", true); return;
        }

        Reservation res = findSelectedReservation(selected);
        if (res == null) { showStatus("Reservation not found.", true); return; }

        long nights = ChronoUnit.DAYS.between(res.getCheckInDate(), res.getCheckOutDate());
        double total = nights * res.getRoom().getType().getPricePerNight();

        PaymentMethod method = PaymentMethod.valueOf(methodStr);
        Invoice invoice = new Invoice(total, method, LocalDate.now());

        // FIX 1: Link invoice to reservation before changing status
        res.setInvoice(invoice);
        // FIX 2: Mark room available again after checkout payment
        res.getRoom().setAvailability(true);
        res.setStatus(ReservationStatus.COMPLETED);

        HotelDatabase.getInvoices().add(invoice);
        // FIX 3: Pass actual reservation ID instead of hardcoded 0
        ReservationDAO.insertPayment(res.getId(), total, methodStr, LocalDate.now());

        // FIX 4: Show the paid invoice before the combo refreshes (status is now COMPLETED)
        invoiceArea.setText(
            "========== INVOICE ==========\n" +
            "Guest    : " + res.getGuest().getUsername() + "\n" +
            "Room     : " + res.getRoom().getRoomNo() + " (" + res.getRoom().getType().getName() + ")\n" +
            "Check-In : " + res.getCheckInDate() + "\n" +
            "Check-Out: " + res.getCheckOutDate() + "\n" +
            "Nights   : " + nights + "\n" +
            "Rate     : $" + res.getRoom().getType().getPricePerNight() + " / night\n" +
            "Method   : " + methodStr + "\n" +
            "Date     : " + LocalDate.now() + "\n" +
            "-----------------------------\n" +
            "TOTAL    : $" + String.format("%.2f", total) + "\n" +
            "Status   : PAID\n" +
            "============================="
        );

        showStatus("Payment of $" + String.format("%.2f", total) + " confirmed via " + methodStr + "!", false);
        loadUnpaidReservations();
    }

    private Reservation findSelectedReservation(String comboValue) {
        Guest guest = SessionManager.getInstance().getLoggedInGuest();
        if (guest == null) return null;
        try {
            String[] parts = comboValue.split(" ");
            int roomNo = Integer.parseInt(parts[1]);
            for (Reservation r : HotelDatabase.getReservations()) {
                if (r.getGuest().getUsername().equals(guest.getUsername())
                        && r.getRoom().getRoomNo() == roomNo
                        && r.getStatus() == ReservationStatus.CONFIRMED) {
                    return r;
                }
            }
        } catch (Exception ignored) {}
        return null;
    }

    @FXML public void goBack() {
        Stage stage = (Stage) invoiceArea.getScene().getWindow();
        SceneNavigator.navigateTo(stage, SceneNavigator.GUEST_DASH);
    }

    private void showStatus(String msg, boolean isError) {
        statusLabel.setText(msg);
        statusLabel.setStyle(isError ? "-fx-text-fill:#e74c3c;" : "-fx-text-fill:#27ae60;");
    }
}
