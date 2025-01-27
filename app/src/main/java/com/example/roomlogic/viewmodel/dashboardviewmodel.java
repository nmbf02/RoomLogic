package com.example.roomlogic.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.roomlogic.model.Reservation;

import java.util.ArrayList;
import java.util.List;

public class dashboardviewmodel extends ViewModel {
    private final MutableLiveData<List<Reservation>> reservations = new MutableLiveData<>();

    public dashboardviewmodel() {
        // Datos ficticios
        List<Reservation> dummyReservations = new ArrayList<>();
        dummyReservations.add(new Reservation("John Doe", "Suite", "123456789", 101));
        dummyReservations.add(new Reservation("Jane Smith", "Deluxe", "987654321", 102));
        dummyReservations.add(new Reservation("Alice Johnson", "Standard", "456123789", 103));
        dummyReservations.add(new Reservation("Bob Brown", "Suite", "789321456", 104));

        reservations.setValue(dummyReservations);
    }

    public LiveData<List<Reservation>> getReservations() {
        return reservations;
    }
}