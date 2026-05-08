package com.hotel.exceptions;
public class AmenityNotFoundException extends RuntimeException {
    public AmenityNotFoundException(String message) {
        super(message);
    }
}