package com.hotel.models.users;
import java.time.LocalDate;
import java.util.ArrayList;
import com.hotel.models.interfaces.Manageable;
import com.hotel.models.rooms.*;
import com.hotel.models.enums.Role;
import com.hotel.exceptions.RoomNotFoundException;
import com.hotel.exceptions.DuplicateRoomException;

public class Admin extends Staff implements Manageable {

    public Admin(String username, String password, LocalDate dateOfBirth, int workingHours) {
        super(username, password, dateOfBirth, Role.ADMIN, workingHours);
    }

    // --- Helper Method to reduce code duplication ---
    private Room findRoom(ArrayList<Room> rooms, int roomNo) {
        for (Room r : rooms) {
            if (r.getRoomNo() == roomNo) {
                return r;
            }
        }
        throw new RoomNotFoundException("Room " + roomNo + " not found.");
    }

    // ==========================================
    // ROOM CRUD OPERATIONS
    // ==========================================

    public void createRoom(ArrayList<Room> rooms, Room newRoom) {
        if (newRoom == null) {
            throw new IllegalArgumentException("Cannot create a null room.");
        }

        // Check for duplicates
        for (Room r : rooms) {
            if (r.getRoomNo() == newRoom.getRoomNo()) {
                throw new DuplicateRoomException("The Room " + newRoom.getRoomNo() + " already exists.");
            }
        }

        rooms.add(newRoom);
    }

    public String readRooms(ArrayList<Room> rooms) {
        // Instead of printing directly, we could return a String or simply return the list.
        // If you need a formatted string for testing/GUI, here is how you do it without print statements:
        if (rooms.isEmpty()) {
            throw new RoomNotFoundException("No Rooms currently exist in the Hotel.");
        }

        StringBuilder sb = new StringBuilder("======= Hotel Rooms =========\n");
        for (Room r : rooms) {
            sb.append(r.toString()).append("\n------------------\n");
        }

        // The calling class (e.g., your Main method or GUI) will handle printing this string
        return sb.toString();
    }

    public void updateRoomType(ArrayList<Room> rooms, int roomNo, RoomType newRoomType) {
        if (newRoomType == null) {
            throw new IllegalArgumentException("New RoomType cannot be null.");
        }

        Room roomToUpdate = findRoom(rooms, roomNo);
        roomToUpdate.setType(newRoomType); // This automatically updates the price and capacity because they are tied to RoomType!
    }

    public void deleteRoom(ArrayList<Room> rooms, int roomNo) {
        // Find the room first to see if it exists
        Room roomToDelete = findRoom(rooms, roomNo);

        // If findRoom didn't throw an exception, it is safe to remove
        rooms.remove(roomToDelete);
    }

    // ==========================================
    // ROOM AMENITY OPERATIONS
    // ==========================================

    public void addAmenityToRoom(ArrayList<Room> rooms, int roomNo, Amenity newAmenity) {
        Room roomToUpdate = findRoom(rooms, roomNo);

        // We let the Room class handle duplicate checks and additions!
        // If it's a duplicate, the Room class will throw a DuplicateAmenityException.
        roomToUpdate.addAmenity(newAmenity);
    }

    public void removeAmenityFromRoom(ArrayList<Room> rooms, int roomNo, Amenity amenityToRemove) {
        Room roomToUpdate = findRoom(rooms, roomNo);

        // We let the Room class handle the removal logic.
        // If not found, the Room class throws an AmenityNotFoundException.
        roomToUpdate.removeAmenity(amenityToRemove);
    }
}