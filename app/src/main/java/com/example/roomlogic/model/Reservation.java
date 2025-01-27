package com.example.roomlogic.model;

public class Reservation {
    private String name;
    private String roomType;
    private String phone;
    private int roomNumber;

    public Reservation(String name, String roomType, String phone, int roomNumber) {
        this.name = name;
        this.roomType = roomType;
        this.phone = phone;
        this.roomNumber = roomNumber;
    }

    public String getName() {
        return name;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getPhone() {
        return phone;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Reservation that = (Reservation) obj;
        return roomNumber == that.roomNumber &&
                name.equals(that.name) &&
                roomType.equals(that.roomType) &&
                phone.equals(that.phone);
    }
}
