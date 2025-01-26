package com.example.roomlogic.repository;

import android.util.Log;

import com.example.roomlogic.repository.ReservationApi;
import com.example.roomlogic.model.Reservation;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationRepository {

    private static ReservationRepository instance;
    private final ReservationApi reservationApi;

    // Constructor privado para evitar instanciación directa
    private ReservationRepository() {
        // Usar el método getInstance de RetrofitClient para obtener Retrofit
        reservationApi = RetrofitClient.getInstance().create(ReservationApi.class);
    }

    // Método para obtener la instancia única (Singleton)
    public static synchronized ReservationRepository getInstance() {
        if (instance == null) {
            instance = new ReservationRepository();
        }
        return instance;
    }

    // Método para obtener las reservas
    public void getReservations(ReservationCallback callback) {
        Call<List<Reservation>> call = reservationApi.getReservations();

        call.enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onComplete(response.body());
                } else {
                    Log.e("ReservationRepository", "Error al obtener reservas: " + response.message());
                    callback.onError("Error al obtener reservas: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {
                Log.e("ReservationRepository", "Fallo de conexión: " + t.getMessage());
                callback.onError("Fallo de conexión: " + t.getMessage());
            }
        });
    }

    // Definición de la interfaz para manejar callbacks
    public interface ReservationCallback {
        void onComplete(List<Reservation> reservationsList);
        void onError(String error);
    }
}
