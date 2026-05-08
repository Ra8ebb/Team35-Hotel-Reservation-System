package com.hotel.exceptions;
public class InvalidReservationDataException extends RuntimeException {
    public InvalidReservationDataException(String message) {
        super(message);
    }
}