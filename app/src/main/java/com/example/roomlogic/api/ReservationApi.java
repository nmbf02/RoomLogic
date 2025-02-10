package com.example.roomlogic.api;

import com.example.roomlogic.model.Reservation;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReservationApi {

    @GET("/api/reservations")
    Call<List<Reservation>> getReservations();

    @GET("/api/reservations/today")  // 🔹 Asegúrate de que esta ruta esté bien configurada en tu backend
    Call<List<Reservation>> getReservationsForToday();

    @POST("/api/reservations")
    Call<Void> createReservation(@Body Reservation reservation);

    @PUT("/api/reservations/{id}")
    Call<Void> updateReservation(@Path("id") int reservationId, @Body Reservation reservation);

    @DELETE("/api/reservations/{id}")
    Call<Void> deleteReservation(@Path("id") int reservationId);
}