package com.hotel.models.bookings;
import com.hotel.models.users.Guest;
import com.hotel.models.rooms.Room;
import java.time.LocalDate;
import com.hotel.models.enums.ReservationStatus;
import com.hotel.models.bookings.Invoice;
import com.hotel.exceptions.InvalidReservationDataException;

public class Reservation {
    // Encapsulated fields (private)
    private Guest guest; // Reference to the Guest making the booking
    private Room room;   // Reference to the booked Room
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private ReservationStatus status;
    private Invoice invoice;   // linked after payment
    private int id = -1;       // database row id, set after insert

    // Constructor to initialize the object and enforce relationships
    public Reservation(Guest guest, Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        setGuest(guest);
        setRoom(room);

        // Validation for dates is handled inside the setCheckOutDate method,
        // but we must set checkInDate first for the comparison to work.
        if (checkInDate == null) {
            throw new InvalidReservationDataException("Check-in date cannot be null.");
        }
        this.checkInDate = checkInDate;

        setCheckOutDate(checkOutDate);

        // A new reservation should naturally start as PENDING
        this.status = ReservationStatus.PENDING;
    }

    // Getters and Setters for Encapsulation
    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        if (guest == null) {
            throw new InvalidReservationDataException("A reservation must have a valid Guest.");
        }
        this.guest = guest;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        if (room == null) {
            throw new InvalidReservationDataException("A reservation must have a valid Room.");
        }
        this.room = room;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        if (checkInDate == null) {
            throw new InvalidReservationDataException("Check-in date cannot be null.");
        }
        // If we are changing the check-in date, we must ensure it doesn't invalidate the checkout date
        if (this.checkOutDate != null && !this.checkOutDate.isAfter(checkInDate)) {
            throw new InvalidReservationDataException("Check-in date must be strictly before the current check-out date.");
        }
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        if (checkOutDate == null) {
            throw new InvalidReservationDataException("Check-out date cannot be null.");
        }
        if (!checkOutDate.isAfter(this.checkInDate)) {
            throw new InvalidReservationDataException("Check-out date must be strictly after the check-in date.");
        }
        this.checkOutDate = checkOutDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        if (status == null) {
            throw new InvalidReservationDataException("Reservation status cannot be null.");
        }
        this.status = status;
    }

    public Invoice getInvoice() { return invoice; }
    public void setInvoice(Invoice invoice) { this.invoice = invoice; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @Override
    public String toString() {
        return "Reservation Details:\n" +
                "- Guest: " + (guest != null ? guest.getUsername() : "Unknown") + "\n" +
                "- Room: " + (room != null ? room.getRoomNo() : "Unknown") + "\n" +
                "- Dates: " + checkInDate + " to " + checkOutDate + "\n" +
                "- Status: " + status;
    }
}