package com.hotel.exceptions;
public class DuplicateAmenityException extends RuntimeException {
    public DuplicateAmenityException(String message) {
        super(message);
    }
}