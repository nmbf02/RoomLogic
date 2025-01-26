package com.example.roomlogic.model;

public class Reservation {
    private String guestName;
    private String phone;
    private int roomNumber;
    private String status;

    // Constructor vacío
    public Reservation() {}

    // Constructor con parámetros
    public Reservation(String guestName, String phone, int roomNumber, String status) {
        this.guestName = guestName;
        this.phone = phone;
        this.roomNumber = roomNumber;
        this.status = status;
    }

    // Getters y setters
    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
