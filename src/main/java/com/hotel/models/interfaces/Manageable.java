package com.hotel.models.interfaces;

import com.hotel.models.rooms.Room;
import com.hotel.models.rooms.RoomType;
import java.util.ArrayList;

public interface Manageable {
    void createRoom(ArrayList<Room> rooms, Room newRoom);
    void deleteRoom(ArrayList<Room> rooms, int roomNo);
    void updateRoomType(ArrayList<Room> rooms, int roomNo, RoomType newRoomType);
}