package com.example.roomlogic.repository;

import com.example.roomlogic.api.RoomApi;
import com.example.roomlogic.model.Room;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomRepository {
    private static RoomRepository instance;
    private final RoomApi roomApi;

    // Constructor privado
    private RoomRepository() {
        roomApi = RetrofitClient.getInstance().create(RoomApi.class);
    }

    // Singleton
    public static synchronized RoomRepository getInstance() {
        if (instance == null) {
            instance = new RoomRepository();
        }
        return instance;
    }

    // Método para obtener habitaciones
    public void fetchRooms(RoomCallback callback) {
        Call<List<Room>> call = roomApi.getRooms();
        call.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error al obtener las habitaciones");
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    // Método para eliminar habitación
    public void deleteRoom(int roomId, DeleteRoomCallback callback) {
        Call<Void> call = roomApi.deleteRoom(roomId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError("Error al eliminar la habitación");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    // Interfaces para manejar los callbacks
    public interface RoomCallback {
        void onSuccess(List<Room> rooms);
        void onError(String error);
    }

    public interface DeleteRoomCallback {
        void onSuccess();
        void onError(String error);
    }
}