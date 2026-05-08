package com.hotel.exceptions;
public class InvalidGuestDataException extends RuntimeException {
    public InvalidGuestDataException(String message) {
        super(message);
    }
}