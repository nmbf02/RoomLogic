package com.example.roomlogic.model;

public class Room {
    private int roomNumber;
    private String type;
    private String status;

    public Room(int roomNumber, String type, String status) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.status = status;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }
}
