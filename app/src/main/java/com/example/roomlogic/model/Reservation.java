package com.example.roomlogic.model;

public class Reservation {
    private String guestName;
    private String phone;
    private int roomNumber;
    private String status;

    public Reservation(String guestName, String phone, int roomNumber, String status) {
        this.guestName = guestName;
        this.phone = phone;
        this.roomNumber = roomNumber;
        this.status = status;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getPhone() {
        return phone;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getStatus() {
        return status;
    }
}