package com.example.roomlogic.model;

public class Room {
    private int id;
    private String room_number; // Cambiado para coincidir con el JSON
    private String room_type;   // Cambiado para coincidir con el JSON
    private double price_per_night; // Cambiado a `double` y para coincidir con el JSON
    private String status;

    // Constructor vacío (necesario si usas bibliotecas como Retrofit o Gson)
    public Room() {
    }

    // Constructor completo
    public Room(int id, String room_number, String room_type, double price_per_night, String status) {
        this.id = id;
        this.room_number = room_number;
        this.room_type = room_type;
        this.price_per_night = price_per_night;
        this.status = status;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return room_number;
    }

    public void setRoomNumber(String room_number) {
        this.room_number = room_number;
    }

    public String getRoomType() {
        return room_type;
    }

    public void setRoomType(String room_type) {
        this.room_type = room_type;
    }

    public double getPricePerNight() {
        return price_per_night;
    }

    public void setPricePerNight(double price_per_night) {
        this.price_per_night = price_per_night;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}