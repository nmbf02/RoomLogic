package com.example.roomlogic.view;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.roomlogic.R;
import com.example.roomlogic.adapter.RoomAdapter;
import com.example.roomlogic.model.Room;
import com.example.roomlogic.repository.RoomRepository;
import java.util.ArrayList;
import android.widget.Toast;

import java.util.List;

public class RoomsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RoomAdapter roomAdapter;
    private List<Room> roomList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        recyclerView = findViewById(R.id.rvRooms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Pasa el contexto junto con la lista al adaptador
        roomAdapter = new RoomAdapter(roomList, this);
        recyclerView.setAdapter(roomAdapter);

        fetchRooms();
    }

    private void fetchRooms() {
        RoomRepository.getInstance().fetchRooms(new RoomRepository.RoomCallback() {
            @Override
            public void onSuccess(List<Room> rooms) {
                roomList.clear();
                roomList.addAll(rooms);
                roomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(RoomsActivity.this, "Error al cargar habitaciones", Toast.LENGTH_SHORT).show();
            }
        });
    }

}