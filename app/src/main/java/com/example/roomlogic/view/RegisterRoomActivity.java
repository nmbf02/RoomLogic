package com.example.roomlogic.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.roomlogic.R;
import com.example.roomlogic.api.ApiClient;
import com.example.roomlogic.api.RoomApi;
import com.example.roomlogic.model.Room;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterRoomActivity extends AppCompatActivity {
    private RoomApi roomApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_room);

        EditText etRoomNumber = findViewById(R.id.etRoomNumber);
        EditText etRoomType = findViewById(R.id.etRoomType);
        Button btnSaveRoom = findViewById(R.id.btnSaveRoom);

        roomApi = ApiClient.getRetrofitInstance().create(RoomApi.class);

        btnSaveRoom.setOnClickListener(v -> {
            String roomNumberStr = etRoomNumber.getText().toString().trim();
            String roomType = etRoomType.getText().toString().trim();

            if (roomNumberStr.isEmpty() || roomType.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Room newRoom = new Room(null, roomNumberStr, roomType, "Available"); // Ahora es String

            roomApi.createRoom(newRoom).enqueue(new Callback<Room>() {
                @Override
                public void onResponse(Call<Room> call, Response<Room> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(RegisterRoomActivity.this, "Habitación registrada exitosamente", Toast.LENGTH_SHORT).show();
                        finish(); // Cierra la actividad
                    } else {
                        Toast.makeText(RegisterRoomActivity.this, "Error al registrar la habitación", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Room> call, Throwable t) {
                    Toast.makeText(RegisterRoomActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}