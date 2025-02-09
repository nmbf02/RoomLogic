package com.example.roomlogic.model;

import com.google.gson.annotations.SerializedName;

public class Room {
    private Integer id;

    @SerializedName("room_number")
    private String roomNumber;

    @SerializedName("room_type")
    private String roomType;

    @SerializedName("status")
    private String status;

    public Room(Integer id, String roomNumber, String roomType, String status) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.status = status;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}