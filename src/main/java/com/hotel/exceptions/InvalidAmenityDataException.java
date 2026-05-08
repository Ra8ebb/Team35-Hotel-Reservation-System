package com.hotel.exceptions;
public class InvalidAmenityDataException extends RuntimeException {
    public InvalidAmenityDataException(String message) {
        super(message);
    }
}