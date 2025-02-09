package com.example.roomlogic.model;

import com.google.gson.annotations.SerializedName;

public class Reservation {
    @SerializedName("id")
    private Integer id;

    @SerializedName("guest_id")
    private int guestId;

    @SerializedName("room_id")
    private int roomId;

    @SerializedName("check_in_date")
    private String checkInDate;

    @SerializedName("check_out_date")
    private String checkOutDate;

    @SerializedName("status")
    private String status;

    public Reservation(Integer id, int guestId, int roomId, String checkInDate, String checkOutDate, String status) {
        this.id = id;
        this.guestId = guestId;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public int getGuestId() {
        return guestId;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public String getStatus() {
        return status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
