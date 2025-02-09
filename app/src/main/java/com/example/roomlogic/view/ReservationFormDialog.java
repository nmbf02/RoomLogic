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
import com.example.roomlogic.api.ReservationApi;
import com.example.roomlogic.model.Reservation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationFormDialog extends Dialog {
    private ReservationApi reservationApi;
    private EditText etGuestId, etRoomId, etCheckInDate, etCheckOutDate, etStatus;
    private Reservation reservation;
    private final OnReservationUpdatedListener onReservationUpdatedListener;

    public ReservationFormDialog(@NonNull Context context, Reservation reservation, OnReservationUpdatedListener listener) {
        super(context);
        this.reservation = reservation;
        this.onReservationUpdatedListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_reservation_form);

        etGuestId = findViewById(R.id.etGuestId);
        etRoomId = findViewById(R.id.etRoomId);
        etCheckInDate = findViewById(R.id.etCheckInDate);
        etCheckOutDate = findViewById(R.id.etCheckOutDate);
        etStatus = findViewById(R.id.etStatus);
        Button btnSave = findViewById(R.id.btnSaveReservation);
        Button btnCancel = findViewById(R.id.btnCancelReservation);

        reservationApi = ApiClient.getReservationApi();

        // Si la reserva no es nueva, llenamos los campos con los datos actuales
        if (reservation != null) {
            etGuestId.setText(String.valueOf(reservation.getGuestId()));
            etRoomId.setText(String.valueOf(reservation.getRoomId()));
            etCheckInDate.setText(reservation.getCheckInDate());
            etCheckOutDate.setText(reservation.getCheckOutDate());
            etStatus.setText(reservation.getStatus());
        }

        // Guardar reserva (crear o actualizar)
        btnSave.setOnClickListener(v -> {
            String guestIdStr = etGuestId.getText().toString().trim();
            String roomIdStr = etRoomId.getText().toString().trim();
            String checkInDate = etCheckInDate.getText().toString().trim();
            String checkOutDate = etCheckOutDate.getText().toString().trim();
            String status = etStatus.getText().toString().trim();

            if (guestIdStr.isEmpty() || roomIdStr.isEmpty() || checkInDate.isEmpty() || checkOutDate.isEmpty() || status.isEmpty()) {
                Toast.makeText(getContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            int guestId = Integer.parseInt(guestIdStr);
            int roomId = Integer.parseInt(roomIdStr);

            Reservation updatedReservation = new Reservation(reservation != null ? reservation.getId() : null, guestId, roomId, checkInDate, checkOutDate, status);

            if (reservation == null) {
                // Crear una nueva reserva
                reservationApi.createReservation(updatedReservation).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Reserva creada correctamente", Toast.LENGTH_SHORT).show();
                            dismiss();
                            onReservationUpdatedListener.onReservationUpdated(updatedReservation);
                        } else {
                            Toast.makeText(getContext(), "Error al crear la reserva", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // Actualizar una reserva existente
                reservationApi.updateReservation(updatedReservation.getId(), updatedReservation).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Reserva actualizada correctamente", Toast.LENGTH_SHORT).show();
                            dismiss();
                            onReservationUpdatedListener.onReservationUpdated(updatedReservation);
                        } else {
                            Toast.makeText(getContext(), "Error al actualizar la reserva", Toast.LENGTH_SHORT).show();
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

    public interface OnReservationUpdatedListener {
        void onReservationUpdated(Reservation updatedReservation);
    }
}