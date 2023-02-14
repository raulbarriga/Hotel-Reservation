package model;

import java.util.Objects;

public class Room implements IRoom {
    private final String roomNumber;
    private final Double roomPrice;
    private final RoomType roomType;
    private final boolean isFree;

    public Room(String roomNumber, RoomType roomType, Double roomPrice) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        isFree = roomPrice == 0;
    }

    @Override
    public String getRoomNumber() {
        return roomNumber;
    }

    @Override
    public RoomType getRoomType() {
        return roomType;
    }

    @Override
    public Double getRoomPrice() {
        return roomPrice;
    }

    @Override
    public boolean isFree() {
        return isFree;
    }

    @Override
    public String toString() {
        return "Room Number: " + roomNumber + " Room Price: $" + roomPrice +
                " Room Type: " + roomType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return roomNumber.equals(room.roomNumber) && roomType == room.roomType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomNumber, roomType);
    }
}
