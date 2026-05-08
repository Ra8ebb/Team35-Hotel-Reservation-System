package com.hotel.models.rooms;
import java.util.ArrayList;
import com.hotel.exceptions.InvalidRoomDataException;
import com.hotel.exceptions.DuplicateAmenityException;
import com.hotel.exceptions.AmenityNotFoundException;

public class Room {

    private int roomNo;
    private int floorNo;
    private RoomType type;
    private ArrayList<Amenity> amenities;
    private boolean isAvailable;

    // Default constructor
    public Room() {
        this.amenities = new ArrayList<>();
    }

    /**
     * Main constructor used to create a valid Room object.
     * Ensures all required data is initialized properly using Custom Exceptions.
     */
    public Room(int roomNo, int floorNo, RoomType type, boolean isAvailable) {
        if (roomNo <= 0) {
            throw new InvalidRoomDataException("Invalid room number. Must be greater than 0.");
        }
        if (floorNo <= 0) {
            throw new InvalidRoomDataException("Invalid floor number. Must be greater than 0.");
        }
        if (type == null) {
            throw new InvalidRoomDataException("Room type cannot be null.");
        }

        this.roomNo = roomNo;
        this.floorNo = floorNo;
        this.type = type;
        this.isAvailable = isAvailable;
        this.amenities = new ArrayList<>();
    }

    // --- Encapsulation: Getters & Setters ---

    public int getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(int roomNo) {
        if (roomNo <= 0) throw new InvalidRoomDataException("Invalid room number.");
        this.roomNo = roomNo;
    }

    public int getFloorNo() {
        return floorNo;
    }

    public void setFloorNo(int floorNo) {
        if (floorNo <= 0) throw new InvalidRoomDataException("Invalid floor number.");
        this.floorNo = floorNo;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        if (type == null) throw new InvalidRoomDataException("Room type cannot be null.");
        this.type = type;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailability(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public ArrayList<Amenity> getAmenities() {
        return amenities;
    }

    // --- Core Behaviors ---

    /**
     * Adds an amenity to the room. Throws an exception if it already exists.
     */
    public void addAmenity(Amenity amenity) {
        if (amenity == null) {
            throw new InvalidRoomDataException("Amenity cannot be null.");
        }

        // Duplicate check (prevents same amenity name in room)
        for (Amenity a : amenities) {
            if (a.getName().equalsIgnoreCase(amenity.getName())) {
                throw new DuplicateAmenityException("Amenity '" + amenity.getName() + "' already exists in this room.");
            }
        }

        amenities.add(amenity);
    }

    /**
     * Removes an amenity from the room by name. Throws an exception if not found.
     */
    public void removeAmenity(Amenity amenity) {
        if (amenities.isEmpty()) {
            throw new AmenityNotFoundException("No amenities available to remove.");
        }

        for (int i = 0; i < amenities.size(); i++) {
            if (amenities.get(i).getName().equalsIgnoreCase(amenity.getName())) {
                amenities.remove(i);
                return; // Exit as soon as we remove it
            }
        }

        // If the loop finishes and nothing was removed
        throw new AmenityNotFoundException("Amenity '" + amenity.getName() + "' not found in this room.");
    }

    /**
     * Replaces the old displayRoomInfo() method.
     * By overriding toString(), the Room object returns its data as a string
     * instead of printing directly to the console, maintaining strict MVC separation.
     */
    @Override
    public String toString() {
        StringBuilder info = new StringBuilder();
        info.append("--- Room Details ---\n")
                .append("Room Number: ").append(roomNo).append("\n")
                .append("Floor Number: ").append(floorNo).append("\n")
                .append("Room Type: ").append(type != null ? type.getName() : "Not set").append("\n")
                .append("Price: $").append(type != null ? type.getPricePerNight() : 0.0).append("\n")
                .append("Status: ").append(isAvailable ? "Available" : "Occupied").append("\n")
                .append("Amenities: ");

        if (amenities.isEmpty()) {
            info.append("No Amenities");
        } else {
            for (int i = 0; i < amenities.size(); i++) {
                info.append(amenities.get(i).getName());
                if (i < amenities.size() - 1) {
                    info.append(", ");
                }
            }
        }
        return info.toString();
    }
}