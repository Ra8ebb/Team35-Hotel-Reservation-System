module com.hotel {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.hotel.app         to javafx.fxml;
    opens com.hotel.controllers to javafx.fxml;

    exports com.hotel.app;
    exports com.hotel.controllers;
    exports com.hotel.database;
    exports com.hotel.models.users;
    exports com.hotel.models.rooms;
    exports com.hotel.models.bookings;
    exports com.hotel.models.enums;
    exports com.hotel.models.interfaces;
    exports com.hotel.network;
    exports com.hotel.threads;
    exports com.hotel.exceptions;
}
