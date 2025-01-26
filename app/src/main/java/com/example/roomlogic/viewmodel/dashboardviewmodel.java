package com.example.roomlogic.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.roomlogic.model.Reservation;

import java.util.ArrayList;
import java.util.List;

public class dashboardviewmodel extends ViewModel {

    private final MutableLiveData<List<Reservation>> reservations = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public dashboardviewmodel() {
        // Inicializa datos ficticios
        loadDummyReservations();
    }

    public LiveData<List<Reservation>> getReservations() {
        return reservations;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void loadDummyReservations() {
        // Simula datos ficticios
        List<Reservation> dummyReservations = new ArrayList<>();
        dummyReservations.add(new Reservation("John Doe", "123456789", 101, "Activo"));
        dummyReservations.add(new Reservation("Jane Smith", "987654321", 102, "Pendiente"));
        dummyReservations.add(new Reservation("Alice Johnson", "456123789", 103, "Cancelado"));
        dummyReservations.add(new Reservation("Bob Brown", "789321456", 104, "Activo"));

        reservations.setValue(dummyReservations);
    }
}