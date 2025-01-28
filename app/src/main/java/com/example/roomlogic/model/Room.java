package com.example.roomlogic.model;

public class Room {
    private Integer id; // Cambiar de int a Integer
    private int roomNumber;
    private String roomType;
    private String status;

    // Constructor
    public Room(Integer id, int roomNumber, String roomType, String status) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.status = status;
    }

    // Getters y setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
