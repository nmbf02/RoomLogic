package com.example.roomlogic.api;

import com.example.roomlogic.model.Room;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RoomApi {
    // Método para obtener todas las habitaciones
    @GET("rooms")
    Call<List<Room>> getAllRooms();

    // Método para obtener una habitación específica por ID
    @GET("rooms/{id}")
    Call<Room> getRoomById(@Path("id") int id);

    // Método para registrar una nueva habitación
    @POST("rooms")
    Call<Room> createRoom(@Body Room room);

    @POST("rooms") // Ajusta el endpoint según tu API
    Call<Void> addRoom(@Body Room room);
}
