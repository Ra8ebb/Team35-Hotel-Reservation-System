package com.hotel.database;

import com.hotel.models.bookings.Invoice;
import com.hotel.models.bookings.Reservation;
import com.hotel.models.enums.Gender;
import com.hotel.models.enums.ReservationStatus;
import com.hotel.models.rooms.Amenity;
import com.hotel.models.rooms.Room;
import com.hotel.models.rooms.RoomType;
import com.hotel.models.users.Admin;
import com.hotel.models.users.Guest;
import com.hotel.models.users.Receptionist;
import com.hotel.models.users.Staff;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * In-memory store for the hotel system.
 * Acts as the single source of truth for all runtime data.
 * Shared between all controllers via static getters.
 */
public class HotelDatabase {

    private static final ArrayList<Guest>       guests       = new ArrayList<>();
    private static final ArrayList<Staff>       staffMembers = new ArrayList<>();
    private static final ArrayList<Room>        rooms        = new ArrayList<>();
    private static final ArrayList<Reservation> reservations = new ArrayList<>();
    private static final ArrayList<Invoice>     invoices     = new ArrayList<>();
    private static final ArrayList<RoomType>    roomTypes    = new ArrayList<>();
    private static final ArrayList<Amenity>     amenities    = new ArrayList<>();

    private static boolean isInitialized = false;

    /**
     * Seeds the in-memory database with realistic dummy data.
     * Call once at application startup.
     */
    public static void initializeDummyData() {
        if (isInitialized) return;

        System.out.println("[DB] Seeding dummy data...");
        try {
            // Room types
            RoomType single = new RoomType(RoomType.TypeName.SINGLE, 100.0, 1);
            RoomType dbl    = new RoomType(RoomType.TypeName.DOUBLE, 150.0, 2);
            RoomType suite  = new RoomType(RoomType.TypeName.SUITE,  300.0, 4);
            roomTypes.add(single); roomTypes.add(dbl); roomTypes.add(suite);

            // Amenities
            Amenity wifi  = new Amenity("Free WiFi");
            Amenity tv    = new Amenity("Smart TV");
            Amenity pool  = new Amenity("Pool Access");
            Amenity gym   = new Amenity("Gym Access");
            Amenity breakfast = new Amenity("Breakfast Included");
            amenities.add(wifi); amenities.add(tv); amenities.add(pool);
            amenities.add(gym);  amenities.add(breakfast);

            // Rooms
            Room r101 = new Room(101, 1, single, true);
            r101.addAmenity(wifi); r101.addAmenity(tv);

            Room r102 = new Room(102, 1, single, true);
            r102.addAmenity(wifi);

            Room r201 = new Room(201, 2, dbl, true);
            r201.addAmenity(wifi); r201.addAmenity(tv); r201.addAmenity(breakfast);

            Room r202 = new Room(202, 2, dbl, true);
            r202.addAmenity(wifi); r202.addAmenity(gym);

            Room r301 = new Room(301, 3, suite, true);
            r301.addAmenity(wifi); r301.addAmenity(tv); r301.addAmenity(pool);
            r301.addAmenity(gym);  r301.addAmenity(breakfast);

            rooms.add(r101); rooms.add(r102);
            rooms.add(r201); rooms.add(r202);
            rooms.add(r301);

            // Staff
            Admin admin = new Admin("admin", "admin123", LocalDate.of(1985, 5, 10), 8);
            Receptionist recep = new Receptionist("recep", "recep123", LocalDate.of(1995, 8, 20), 8);
            staffMembers.add(admin); staffMembers.add(recep);

            // Guests
            Guest g1 = new Guest("Mario", "1234", LocalDate.of(1990, 1, 1),
                    "Ain shams", Gender.MALE, RoomType.TypeName.SINGLE);
            Guest g2 = new Guest("janedoe", "pass456", LocalDate.of(1992, 4, 15),
                    "456 Oak Ave", Gender.FEMALE, RoomType.TypeName.SUITE);
            guests.add(g1); guests.add(g2);

            // One existing reservation for demo purposes
            Reservation res = new Reservation(g1, r101, LocalDate.now(), LocalDate.now().plusDays(3));
            res.setStatus(ReservationStatus.CONFIRMED);
            r101.setAvailability(false);
            reservations.add(res);

            isInitialized = true;
            System.out.println("[DB] Data loaded successfully.");
        } catch (Exception e) {
            System.err.println("[DB] Error seeding data: " + e.getMessage());
        }
    }

    // --- Static Getters ---
    public static ArrayList<Guest>       getGuests()       { return guests; }
    public static ArrayList<Staff>       getStaffMembers() { return staffMembers; }
    public static ArrayList<Room>        getRooms()        { return rooms; }
    public static ArrayList<Reservation> getReservations() { return reservations; }
    public static ArrayList<Invoice>     getInvoices()     { return invoices; }
    public static ArrayList<RoomType>    getRoomTypes()    { return roomTypes; }
    public static ArrayList<Amenity>     getAmenities()    { return amenities; }

    /** Convenience lookup: find a Guest by username, returns null if not found. */
    public static Guest findGuest(String username) {
        for (Guest g : guests) {
            if (g.getUsername().equalsIgnoreCase(username)) return g;
        }
        return null;
    }

    /** Convenience lookup: find a Staff by username, returns null if not found. */
    public static Staff findStaff(String username) {
        for (Staff s : staffMembers) {
            if (s.getUsername().equalsIgnoreCase(username)) return s;
        }
        return null;
    }
}
