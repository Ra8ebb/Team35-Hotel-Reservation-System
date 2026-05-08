package com.hotel.models.interfaces;

public interface Payable {
    // A contract stating that anything implementing this must be able to calculate its final total
    double calculateFinalAmount();
    boolean isPaid();
}