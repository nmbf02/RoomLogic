package com.example.roomlogic.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.roomlogic.model.Reservation;
import com.example.roomlogic.repository.ReservationRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class dashboardviewmodel extends ViewModel {

    private final MutableLiveData<List<Reservation>> reservations = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final ReservationRepository reservationRepository;

    public dashboardviewmodel() {
        // Inicializa el repositorio
        reservationRepository = ReservationRepository.getInstance();
    }

    // Método para obtener las reservas como LiveData
    public LiveData<List<Reservation>> getReservations() {
        return reservations;
    }

    // Método para obtener mensajes de error como LiveData
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    // Método para cargar reservas desde el repositorio (usando Retrofit)
    public void fetchReservations() {
        reservationRepository.getReservations(new ReservationRepository.ReservationCallback() {
            @Override
            public void onComplete(List<Reservation> reservationsList) {
                if (reservationsList != null) {
                    reservations.setValue(reservationsList); // Actualiza las reservas
                } else {
                    errorMessage.setValue("Error al obtener reservas: datos vacíos");
                }
            }

            @Override
            public void onError(String error) {
                errorMessage.setValue("Error al obtener reservas: " + error);
            }
        });
    }

    // Método alternativo para cargar reservas (si no usas Retrofit, sino una interfaz directa al repositorio)
    public void loadReservations() {
        reservationRepository.getReservations(new ReservationRepository.ReservationCallback() {
            @Override
            public void onComplete(List<Reservation> reservationsList) {
                reservations.setValue(reservationsList); // Actualiza las reservas en LiveData
            }

            @Override
            public void onError(String error) {
                errorMessage.setValue("Error al obtener reservas: " + error);
            }
        });
    }


}
