package com.hotel.models.users;
import java.time.LocalDate;
import com.hotel.models.users.Receptionist;
import com.hotel.models.bookings.Reservation;
import com.hotel.models.enums.Role;
import com.hotel.models.enums.ReservationStatus;
import com.hotel.exceptions.InvalidReservationDataException;
import com.hotel.exceptions.RoomNotAvailableException;

public class Receptionist extends Staff {

    public Receptionist(String username, String password, LocalDate dateOfBirth, int workingHours) {
        super(username, password, dateOfBirth, Role.RECEPTIONIST, workingHours);
    }

    /**
     * Handles the check-in process.
     * Updates reservation status to CONFIRMED and marks the room as occupied.
     */
    public void manageCheckIn(Reservation reservation) {
        if (reservation == null) {
            throw new InvalidReservationDataException("Cannot process check-in: Reservation is null.");
        }

        // Prevent check-in if the room is already occupied
        if (!reservation.getRoom().isAvailable()) {
            // Swapped to the correct exception name!
            throw new RoomNotAvailableException("Error: Room " + reservation.getRoom().getRoomNo() + " is already occupied!");
        }

        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.getRoom().setAvailability(false); // Room is no longer available
    }

    /**
     * Handles the check-out process.
     * Updates reservation status to COMPLETED and frees up the room.
     */
    public void manageCheckOut(Reservation reservation) {
        if (reservation == null) {
            throw new InvalidReservationDataException("Cannot process check-out: Reservation is null.");
        }

        reservation.setStatus(ReservationStatus.COMPLETED);
        reservation.getRoom().setAvailability(true); // Room is now available for the next guest
    }
}