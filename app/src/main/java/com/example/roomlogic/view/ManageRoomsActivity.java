package com.example.roomlogic.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.roomlogic.R;
import com.example.roomlogic.adapter.RoomAdapter;
import com.example.roomlogic.api.ApiClient;
import com.example.roomlogic.api.RoomApi;
import com.example.roomlogic.model.Room;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageRoomsActivity extends AppCompatActivity {
    private RecyclerView recyclerViewRooms;
    private RoomAdapter roomAdapter;
    private RoomApi roomApi;
    private List<Room> roomList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_rooms);

        recyclerViewRooms = findViewById(R.id.recyclerViewRooms);
        recyclerViewRooms.setLayoutManager(new LinearLayoutManager(this));

        roomList = new ArrayList<>();
        roomAdapter = new RoomAdapter(roomList, this);
        recyclerViewRooms.setAdapter(roomAdapter);

        roomApi = ApiClient.getRoomApi();

        // Botón para agregar habitación
        Button btnAddRoom = findViewById(R.id.btnAddRoom);
        btnAddRoom.setOnClickListener(v -> {
            RoomFormDialog dialog = new RoomFormDialog(this, null, newRoom -> {
                roomList.add(newRoom);
                roomAdapter.notifyDataSetChanged();
            });
            dialog.show();
        });

        // Cargar habitaciones
        loadRooms();
    }

    private void loadRooms() {
        roomApi.getRooms().enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    roomList.clear();
                    roomList.addAll(response.body());
                    roomAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ManageRoomsActivity.this, "Error al obtener habitaciones", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                Toast.makeText(ManageRoomsActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showRoomFormDialog(Room room) {
        RoomFormDialog dialog = new RoomFormDialog(this, room, updatedRoom -> {
            int index = roomList.indexOf(room);
            if (index != -1) {
                roomList.set(index, updatedRoom);
                roomAdapter.notifyDataSetChanged();
            }
        });
        dialog.show();
    }
}