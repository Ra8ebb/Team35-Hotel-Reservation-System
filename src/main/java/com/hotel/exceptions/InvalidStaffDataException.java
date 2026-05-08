package com.hotel.exceptions;
public class InvalidStaffDataException extends RuntimeException {
    public InvalidStaffDataException(String message) {
        super(message);
    }
}