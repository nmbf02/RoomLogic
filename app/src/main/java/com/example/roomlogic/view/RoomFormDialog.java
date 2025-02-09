package com.example.roomlogic.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.roomlogic.R;
import com.example.roomlogic.api.ApiClient;
import com.example.roomlogic.api.RoomApi;
import com.example.roomlogic.model.Room;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomFormDialog extends Dialog {
    private RoomApi roomApi;
    private EditText etRoomNumber, etRoomType, etRoomStatus;
    private Room room;
    private final OnRoomUpdatedListener onRoomUpdatedListener;

    public RoomFormDialog(@NonNull Context context, Room room, OnRoomUpdatedListener listener) {
        super(context);
        this.room = room;
        this.onRoomUpdatedListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_room_form);

        etRoomNumber = findViewById(R.id.etRoomNumber);
        etRoomType = findViewById(R.id.etRoomType);
        etRoomStatus = findViewById(R.id.etRoomStatus);
        Button btnSave = findViewById(R.id.btnSaveRoom);
        Button btnCancel = findViewById(R.id.btnCancelRoom);

        roomApi = ApiClient.getRetrofitInstance().create(RoomApi.class);

        // Si la habitación no es nueva, rellena los campos con los datos actuales
        if (room != null) {
            etRoomNumber.setText(room.getRoomNumber());
            etRoomType.setText(room.getRoomType());
            etRoomStatus.setText(room.getStatus());
        }

        // Guardar los cambios o crear nueva habitación
        btnSave.setOnClickListener(v -> {
            String number = etRoomNumber.getText().toString().trim();
            String type = etRoomType.getText().toString().trim();
            String status = etRoomStatus.getText().toString().trim();

            if (number.isEmpty() || type.isEmpty() || status.isEmpty()) {
                Toast.makeText(getContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            if (room == null) {
                // Crear una nueva habitación
                Room newRoom = new Room(null, number, type, status);
                roomApi.createRoom(newRoom).enqueue(new Callback<Room>() {
                    @Override
                    public void onResponse(Call<Room> call, Response<Room> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Habitación creada correctamente", Toast.LENGTH_SHORT).show();
                            dismiss();
                            onRoomUpdatedListener.onRoomUpdated(response.body()); // Notificar la actualización
                        } else {
                            Toast.makeText(getContext(), "Error al crear la habitación", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Room> call, Throwable t) {
                        Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // Actualizar la habitación existente
                Room updatedRoom = new Room(room.getId(), number, type, status);
                roomApi.updateRoom(updatedRoom.getId(), updatedRoom).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Habitación actualizada", Toast.LENGTH_SHORT).show();
                            dismiss();
                            onRoomUpdatedListener.onRoomUpdated(updatedRoom);
                        } else {
                            Toast.makeText(getContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnCancel.setOnClickListener(v -> dismiss());
    }

    public interface OnRoomUpdatedListener {
        void onRoomUpdated(Room updatedRoom);
    }
}