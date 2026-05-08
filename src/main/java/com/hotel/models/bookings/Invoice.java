package com.hotel.models.bookings;
import com.hotel.models.interfaces.Payable;
import com.hotel.models.enums.PaymentMethod;
import java.time.LocalDate;
import com.hotel.exceptions.InvalidPaymentException;

public class Invoice implements Payable {
    private double totalAmount;
    private PaymentMethod paymentMethod;
    private LocalDate paymentDate;

    // Constructor to initialize the invoice
    public Invoice(double totalAmount, PaymentMethod paymentMethod, LocalDate paymentDate) {
        // Route through setters to ensure validation logic is applied consistently
        setTotalAmount(totalAmount);
        setPaymentMethod(paymentMethod);
        setPaymentDate(paymentDate);
    }

    // Getters and Setters
    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        // Validation: The total amount cannot be negative
        if (totalAmount < 0) {
            throw new InvalidPaymentException("Total amount cannot be negative.");
        }
        this.totalAmount = totalAmount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            throw new InvalidPaymentException("Payment method cannot be null.");
        }
        this.paymentMethod = paymentMethod;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        if (paymentDate == null) {
            throw new InvalidPaymentException("Payment date cannot be null.");
        }
        this.paymentDate = paymentDate;
    }

    @Override
    public String toString() {
        return "Invoice Details:\n" +
                "- Date: " + paymentDate + "\n" +
                "- Amount: $" + String.format("%.2f", totalAmount) + "\n" +
                "- Method: " + paymentMethod;
    }

    @Override
    public double calculateFinalAmount() {
        return totalAmount;
    }

    @Override
    public boolean isPaid() {
        return totalAmount > 0 && paymentMethod != null && paymentDate != null;
    }
}