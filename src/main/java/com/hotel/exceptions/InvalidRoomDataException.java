package com.hotel.exceptions;
public class InvalidRoomDataException extends RuntimeException {
    public InvalidRoomDataException(String message) {
        super(message);
    }
}