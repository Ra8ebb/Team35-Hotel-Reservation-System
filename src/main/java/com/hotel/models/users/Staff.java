package com.hotel.models.users;
import java.time.LocalDate;
import java.util.ArrayList;
import com.hotel.models.enums.Role;
import com.hotel.models.enums.ReservationStatus;
import com.hotel.models.bookings.Reservation;
import com.hotel.models.rooms.Room;
import com.hotel.exceptions.InvalidStaffDataException;
import com.hotel.exceptions.DataNotFoundException;
public abstract class Staff {
    private String username;
    private String password;
    private LocalDate dateOfBirth;
    private Role role;
    private int workingHours;

    public Staff(String username, String password, LocalDate dateOfBirth, Role role, int workingHours) {
        // Routing through setters for validation
        setUsername(username);
        setPassword(password);
        setDateOfBirth(dateOfBirth);
        setRole(role);
        setWorkingHours(workingHours);
    }

    // --- Getters & Setters with Validation ---

    public String getUsername() { return username; }
    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new InvalidStaffDataException("Username cannot be empty.");
        }
        this.username = username;
    }

    public String getPassword() { return password; }
    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new InvalidStaffDataException("Password cannot be empty.");
        }
        this.password = password;
    }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.isAfter(LocalDate.now())) {
            throw new InvalidStaffDataException("Invalid Date of Birth.");
        }
        this.dateOfBirth = dateOfBirth;
    }

    public Role getRole() { return role; }
    public void setRole(Role role) {
        if (role == null) {
            throw new InvalidStaffDataException("Role cannot be null.");
        }
        this.role = role;
    }

    public int getWorkingHours() { return workingHours; }
    public void setWorkingHours(int workingHours) {
        if (workingHours < 0) {
            throw new InvalidStaffDataException("Working hours cannot be negative.");
        }
        this.workingHours = workingHours;
    }

    // --- Shared Staff Behaviors ---

    /**
     * Returns a formatted string of all guests in the system.
     */
    public String viewAllGuests(ArrayList<Guest> guests) {
        if (guests == null || guests.isEmpty()) {
            throw new DataNotFoundException("There are no Guests in the system.");
        }

        StringBuilder sb = new StringBuilder("====== Guests in the system ======\n");
        for (Guest g : guests) {
            if (g != null) {
                sb.append("Username : ").append(g.getUsername()).append("\n")
                        .append("Date Of Birth : ").append(g.getDateOfBirth()).append("\n")
                        // Assuming Guest has these getters, if not, we will add them when we do the Guest class!
                        .append("Address : ").append(g.getAddress()).append("\n")
                        .append("Gender : ").append(g.getGender()).append("\n")
                        .append("Room Preference : ").append(g.getRoomPreference()).append("\n")
                        .append("-------------------------------------------------\n");
            }
        }
        return sb.toString();
    }

    /**
     * Returns a formatted string of all rooms using the Room class's toString().
     */
    public String viewAllRooms(ArrayList<Room> rooms) {
        if (rooms == null || rooms.isEmpty()) {
            throw new DataNotFoundException("There are no Rooms in the system.");
        }

        StringBuilder sb = new StringBuilder("====== The Hotel Rooms ======\n");
        for (Room r : rooms) {
            if (r != null) {
                sb.append(r.toString()).append("\n\n"); // r.toString() replaces r.displayRoomInfo()
            }
        }
        return sb.toString();
    }

    /**
     * Returns a formatted string of all active reservations (ignores CANCELLED).
     */
    public String viewAllReservations(ArrayList<Reservation> reservations) {
        if (reservations == null || reservations.isEmpty()) {
            throw new DataNotFoundException("There are no Reservations in the system.");
        }

        StringBuilder sb = new StringBuilder("====== The Hotel Reservations ======\n");
        boolean hasReservation = false;

        for (Reservation r : reservations) {
            if (r != null && r.getStatus() != ReservationStatus.CANCELLED) {
                sb.append("Guest: ").append(r.getGuest().getUsername()).append("\n")
                        .append("Room Number: ").append(r.getRoom().getRoomNo()).append("\n")
                        .append("Status: ").append(r.getStatus()).append("\n")
                        .append("--------------------------------------\n");
                hasReservation = true;
            }
        }

        if (!hasReservation) {
            throw new DataNotFoundException("There are no active Reservations in the system.");
        }

        return sb.toString();
    }
}