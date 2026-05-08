# Hotel Reservation System

A JavaFX-based hotel management GUI with SQLite persistence, multi-threading,
and basic socket-based live chat.

---

## 📁 Project Structure

```
Team-35-hotel-reservation-system/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/
    │   │   ├── module-info.java
    │   │   └── com/hotel/
    │   │       ├── app/
    │   │       │   ├── Main.java                  ← JavaFX Application entry point
    │   │       │   ├── SessionManager.java         ← Stores logged-in user across screens
    │   │       │   └── SceneNavigator.java         ← Centralized scene switching
    │   │       ├── controllers/
    │   │       │   ├── LoginController.java
    │   │       │   ├── RegisterController.java
    │   │       │   ├── GuestDashboardController.java
    │   │       │   ├── RoomBrowseController.java
    │   │       │   ├── ReservationsController.java
    │   │       │   ├── PaymentController.java
    │   │       │   ├── ChatController.java
    │   │       │   └── StaffDashboardController.java
    │   │       ├── database/
    │   │       │   ├── DatabaseManager.java        ← SQLite schema creation
    │   │       │   ├── HotelDatabase.java          ← In-memory data store + dummy data
    │   │       │   └── ReservationDAO.java         ← SQL insert/update helpers
    │   │       ├── exceptions/                     ← (unchanged from M1)
    │   │       ├── models/                         ← (unchanged from M1)
    │   │       │   ├── bookings/ (Invoice, Reservation)
    │   │       │   ├── enums/   (Gender, PaymentMethod, ReservationStatus, Role)
    │   │       │   ├── interfaces/ (Manageable, Payable)
    │   │       │   ├── rooms/   (Amenity, Room, RoomType)
    │   │       │   └── users/   (Admin, Guest, Receptionist, Staff)
    │   │       ├── network/
    │   │       │   ├── ChatServer.java             ← Multi-client socket server
    │   │       │   └── ChatClient.java             ← Client socket + message reader
    │   │       ├── threads/
    │   │       │   ├── CSVExportService.java       ← (unchanged from M1)
    │   │       │   └── RoomAvailabilityService.java← Background room refresh
    │   │       └── utils/
    │   │           └── Validation.java             ← (unchanged from M1)
    │   └── resources/
    │       ├── com/hotel/views/
    │       │   ├── Login.fxml
    │       │   ├── Register.fxml
    │       │   ├── GuestDashboard.fxml
    │       │   ├── RoomBrowse.fxml
    │       │   ├── Reservations.fxml
    │       │   ├── Payment.fxml
    │       │   ├── Chat.fxml
    │       │   └── StaffDashboard.fxml
    │       └── css/
    │           └── hotel.css
    └── test/
```

---

## ▶️ How to Run

### Prerequisites
- Java 17 or higher
- Maven 3.8+

### Steps

**1. Clone and build:**
```bash
git clone <repo-url>
cd Team-35-hotel-reservation-system
mvn clean install
```

**2. (Optional) Start the Chat Server first** (in a separate terminal):
```bash
mvn exec:java -Dexec.mainClass="com.hotel.network.ChatServer"
```

**3. Launch the main application:**
```bash
mvn javafx:run
```

### Demo Credentials
| Role        | Username | Password  |
|-------------|----------|-----------|
| Guest       | Mario    | 1234      |
| Guest       | janedoe  | pass456   |
| Admin       | admin    | admin123  |
| Receptionist| recep    | recep123  |

---

## 🧪 Test Scenarios

### Authentication
| Scenario           | Input                        | Expected Result                     |
|--------------------|------------------------------|-------------------------------------|
| Valid guest login  | johndoe / pass123            | Redirects to Guest Dashboard        |
| Invalid login      | wronguser / wrongpass        | Red error message shown             |
| Staff login        | admin / admin123             | Redirects to Staff Dashboard        |
| Register new guest | Fill all fields correctly    | Success message; account created    |
| Register duplicate | Existing username            | "Username already exists" error     |
| Register mismatch  | Passwords don't match        | Error shown, no account created     |

### Reservations
| Scenario                 | Input                              | Expected Result                  |
|--------------------------|------------------------------------|----------------------------------|
| Create reservation       | Available room + future dates      | Room marked occupied; listed     |
| Past check-in date       | Check-in = yesterday               | Validation error message         |
| Invalid dates            | Check-out before check-in          | Validation error                 |
| Cancel reservation       | Select active booking → Cancel     | Status changes to CANCELLED      |

### Payment
| Scenario             | Input                              | Expected Result               |
|----------------------|------------------------------------|-------------------------------|
| Generate invoice     | Confirmed reservation selected     | Invoice shown in text area     |
| Confirm payment      | Choose method → Confirm            | Status → COMPLETED, confirmed  |
| No reservation       | Nothing selected                   | Error message shown            |

### Chat
| Scenario             | Setup                              | Expected Result                    |
|----------------------|------------------------------------|------------------------------------|
| Server running       | ChatServer main running, then login| Messages appear in chat area       |
| Server offline       | No server started                  | "Server offline" shown in chat area|
| Send message         | Type + click Send or press Enter   | Message broadcast to all clients   |

### Threading
| Scenario                    | Expected Behavior                                          |
|-----------------------------|------------------------------------------------------------|
| Room Browse auto-refresh    | Status label updates every 10s without freezing the UI     |
| CSV export (background)     | CSV files appear in project root every 30 seconds          |

---
