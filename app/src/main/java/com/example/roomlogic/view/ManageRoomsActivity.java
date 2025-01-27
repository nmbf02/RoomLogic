package com.example.roomlogic.view;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomlogic.R;
import com.example.roomlogic.adapter.RoomAdapter;
import com.example.roomlogic.model.Room;

import java.util.ArrayList;
import java.util.List;

public class ManageRoomsActivity extends AppCompatActivity {
    private List<Room> roomList = new ArrayList<>();
    private RoomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_rooms);

        RecyclerView rvRooms = findViewById(R.id.rvRooms);
        rvRooms.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar el adaptador
        adapter = new RoomAdapter(roomList, new RoomAdapter.OnRoomActionListener() {
            @Override
            public void onEdit(Room room) {
                Toast.makeText(ManageRoomsActivity.this, "Editar: " + room.getRoomNumber(), Toast.LENGTH_SHORT).show();
                // Lógica para editar habitación
            }

            @Override
            public void onDelete(Room room) {
                Toast.makeText(ManageRoomsActivity.this, "Eliminar: " + room.getRoomNumber(), Toast.LENGTH_SHORT).show();
                // Lógica para eliminar habitación
            }
        });

        rvRooms.setAdapter(adapter);

        findViewById(R.id.btnAddRoom).setOnClickListener(v -> {
            Toast.makeText(this, "Agregar nueva habitación", Toast.LENGTH_SHORT).show();
            // Lógica para agregar nueva habitación
        });

        // Simulación de datos para prueba
        loadMockData();
    }

    private void loadMockData() {
        roomList.add(new Room(1, 101, "Suite", "Disponible"));
        roomList.add(new Room(2, 102, "Deluxe", "Ocupado"));
        roomList.add(new Room(3, 103, "Estandar", "Mantenimiento"));
        adapter.notifyDataSetChanged();
    }
}