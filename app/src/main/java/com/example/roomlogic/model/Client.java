package com.example.roomlogic.model;

import com.google.gson.annotations.SerializedName;

public class Client {
    private Integer id; // ID puede ser null
    private String name;
    private String email;

    @SerializedName("phone_number") // Asegura compatibilidad con la API en Rust
    private String phone;

    private String address;
    private String status;

    // Constructor con todos los parámetros
    public Client(Integer id, String name, String email, String phone, String address, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.status = status;
    }

    // Getters y setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone; // Sigue usando "phone" en Android
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}