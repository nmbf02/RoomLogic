package com.example.roomlogic.api;

import com.example.roomlogic.model.Room;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RoomApi {
    @GET("/api/rooms")
    Call<List<Room>> getRooms();

    @POST("/api/rooms")
    Call<Room> createRoom(@Body Room room);

    @DELETE("/api/rooms/{id}")
    Call<Void> deleteRoom(@Path("id") int roomId);

    @PUT("/api/rooms/{id}")
    Call<Void> updateRoom(@Path("id") int roomId, @Body Room room);
}