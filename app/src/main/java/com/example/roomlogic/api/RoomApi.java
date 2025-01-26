package com.example.roomlogic.api;

import com.example.roomlogic.model.Room;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RoomApi {
    @GET("rooms")
    Call<List<Room>> getRooms();

    @DELETE("rooms/{id}")
    Call<Void> deleteRoom(@Path("id") int roomId);
}
