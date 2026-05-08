package com.hotel.exceptions;
public class InvalidRoomTypeDataException extends RuntimeException {
    public InvalidRoomTypeDataException(String message) {
        super(message);
    }
}