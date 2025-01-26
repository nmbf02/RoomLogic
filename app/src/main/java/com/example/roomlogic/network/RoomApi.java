package com.example.roomlogic.network;

import com.example.roomlogic.model.Room;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RoomApi {

    // Endpoint para obtener todas las habitaciones
    @GET("rooms")
    Call<List<Room>> getRooms();

    // Endpoint para eliminar una habitación por ID
    @DELETE("rooms/{id}")
    Call<Void> deleteRoom(@Path("id") int roomId);
}