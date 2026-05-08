package com.hotel.app;

import com.hotel.models.users.Guest;
import com.hotel.models.users.Staff;

/**
 * Singleton that stores the currently authenticated user for the session.
 * Controllers read from here instead of passing objects between scenes.
 */
public class SessionManager {

    private static SessionManager instance;

    private Guest loggedInGuest;
    private Staff loggedInStaff;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public Guest getLoggedInGuest() { return loggedInGuest; }
    public void setLoggedInGuest(Guest guest) {
        this.loggedInGuest = guest;
        this.loggedInStaff = null; // only one type active at a time
    }

    public Staff getLoggedInStaff() { return loggedInStaff; }
    public void setLoggedInStaff(Staff staff) {
        this.loggedInStaff = staff;
        this.loggedInGuest = null;
    }

    public boolean isGuestLoggedIn() { return loggedInGuest != null; }
    public boolean isStaffLoggedIn() { return loggedInStaff != null; }

    public void logout() {
        loggedInGuest = null;
        loggedInStaff = null;
    }
}
