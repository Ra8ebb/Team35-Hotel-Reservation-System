package com.hotel.models.users;
import java.time.LocalDate;
import com.hotel.models.enums.Gender;
import com.hotel.models.rooms.RoomType;
import com.hotel.exceptions.InvalidGuestDataException;
public class Guest {
    // Encapsulated Fields
    private String username;
    private String password;
    private LocalDate dateOfBirth;
    private String address;
    private Gender gender;
    private RoomType.TypeName roomPreference;

    /**
     * Full constructor to initialize a new Guest.
     * Routes all inputs through setters to enforce validation rules.
     */
    public Guest(String username, String password, LocalDate dateOfBirth, String address, Gender gender, RoomType.TypeName roomPreference) {
        setUsername(username);
        setPassword(password);
        setDateOfBirth(dateOfBirth);
        setAddress(address);
        setGender(gender);
        setRoomPreference(roomPreference);
    }

    // --- Getters and Setters with Validation ---

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new InvalidGuestDataException("Username cannot be empty.");
        }
        this.username = username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new InvalidGuestDataException("Password cannot be empty.");
        }
        this.password = password;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            throw new InvalidGuestDataException("Date of Birth cannot be null.");
        }
        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new InvalidGuestDataException("Date of Birth cannot be in the future.");
        }
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new InvalidGuestDataException("Address cannot be empty.");
        }
        this.address = address.trim();
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        if (gender == null) {
            throw new InvalidGuestDataException("Gender cannot be null.");
        }
        this.gender = gender;
    }

    public RoomType.TypeName getRoomPreference() {
        return roomPreference;
    }

    public void setRoomPreference(RoomType.TypeName roomPreference) {
        if (roomPreference == null) {
            throw new InvalidGuestDataException("Room preference cannot be null.");
        }
        this.roomPreference = roomPreference;
    }

    // --- Core Behaviors ---

    /**
     * Provides a beautifully formatted string representation of the Guest.
     * This matches perfectly with the viewAllGuests() method we built in the Staff class!
     */
    @Override
    public String toString() {
        return "Username: " + username + "\n" +
                "Date Of Birth: " + dateOfBirth + "\n" +
                "Address: " + address + "\n" +
                "Gender: " + gender + "\n" +
                "Room Preference: " + roomPreference;
    }
}