package com.example.roomlogic.repository;

import com.example.roomlogic.model.Reservation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ReservationApi {
    @GET("reservations")
    Call<List<Reservation>> getReservations();
}
