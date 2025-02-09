package com.example.roomlogic.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.roomlogic.R;
import com.example.roomlogic.api.ApiClient;
import com.example.roomlogic.api.ReservationApi;
import com.example.roomlogic.api.RoomApi;
import com.example.roomlogic.api.ClientApi;
import com.example.roomlogic.model.Client;
import com.example.roomlogic.model.Reservation;
import com.example.roomlogic.model.Room;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationFormDialog extends Dialog {
    private ReservationApi reservationApi;
    private ClientApi clientApi;
    private RoomApi roomApi;
    private Spinner spinnerClient, spinnerRoom, spinnerStatus;
    private EditText etCheckInDate, etCheckOutDate;
    private List<Client> clientList = new ArrayList<>();
    private List<Room> roomList = new ArrayList<>();
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

        spinnerClient = findViewById(R.id.spinnerClient);
        spinnerRoom = findViewById(R.id.spinnerRoom);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        etCheckInDate = findViewById(R.id.etCheckInDate);
        etCheckOutDate = findViewById(R.id.etCheckOutDate);
        Button btnSave = findViewById(R.id.btnSaveReservation);
        Button btnCancel = findViewById(R.id.btnCancelReservation);

        reservationApi = ApiClient.getRetrofitInstance().create(ReservationApi.class);
        clientApi = ApiClient.getRetrofitInstance().create(ClientApi.class);
        roomApi = ApiClient.getRetrofitInstance().create(RoomApi.class);

        loadClients();
        loadRooms();

        // Configurar Spinner de estados de reserva
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(
                getContext(), R.array.reservation_status, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusAdapter);

        if (reservation != null) {
            setSpinnerSelection(spinnerStatus, reservation.getStatus());
            etCheckInDate.setText(reservation.getCheckInDate());
            etCheckOutDate.setText(reservation.getCheckOutDate());
        }

        // Abrir el DatePicker cuando se toca el campo de Check-in
        etCheckInDate.setOnClickListener(v -> showDatePickerDialog(etCheckInDate));

        // Abrir el DatePicker cuando se toca el campo de Check-out
        etCheckOutDate.setOnClickListener(v -> showDatePickerDialog(etCheckOutDate));

        btnSave.setOnClickListener(v -> saveReservation());
        btnCancel.setOnClickListener(v -> dismiss());
    }

    private void showDatePickerDialog(EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Formatear la fecha seleccionada en YYYY-MM-DD
                    String selectedDate = selectedYear + "-" + String.format("%02d", (selectedMonth + 1)) + "-" + String.format("%02d", selectedDay);
                    editText.setText(selectedDate);
                }, year, month, day);

        datePickerDialog.show();
    }

    private void loadClients() {
        clientApi.getClients().enqueue(new Callback<List<Client>>() {
            @Override
            public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    clientList = response.body();
                    ArrayAdapter<String> clientAdapter = new ArrayAdapter<>(getContext(),
                            android.R.layout.simple_spinner_item,
                            getClientNames());
                    clientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerClient.setAdapter(clientAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Client>> call, Throwable t) {
                Toast.makeText(getContext(), "Error al cargar clientes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadRooms() {
        roomApi.getRooms().enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    roomList = response.body();
                    ArrayAdapter<String> roomAdapter = new ArrayAdapter<>(getContext(),
                            android.R.layout.simple_spinner_item,
                            getRoomNumbers());
                    roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerRoom.setAdapter(roomAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                Toast.makeText(getContext(), "Error al cargar habitaciones", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<String> getClientNames() {
        List<String> names = new ArrayList<>();
        for (Client client : clientList) {
            names.add(client.getId() + " - " + client.getName());
        }
        return names;
    }

    private List<String> getRoomNumbers() {
        List<String> rooms = new ArrayList<>();
        for (Room room : roomList) {
            rooms.add(room.getId() + " - " + room.getRoomNumber());
        }
        return rooms;
    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        if (adapter != null) {
            int position = adapter.getPosition(value);
            if (position >= 0) {
                spinner.setSelection(position);
            }
        }
    }

    private void saveReservation() {
        int selectedClientIndex = spinnerClient.getSelectedItemPosition();
        int selectedRoomIndex = spinnerRoom.getSelectedItemPosition();
        String checkInDate = etCheckInDate.getText().toString().trim();
        String checkOutDate = etCheckOutDate.getText().toString().trim();
        String status = spinnerStatus.getSelectedItem().toString();

        if (selectedClientIndex < 0 || selectedRoomIndex < 0 || checkInDate.isEmpty() || checkOutDate.isEmpty()) {
            Toast.makeText(getContext(), "Selecciona todos los campos correctamente", Toast.LENGTH_SHORT).show();
            return;
        }

        int clientId = clientList.get(selectedClientIndex).getId();
        int roomId = roomList.get(selectedRoomIndex).getId();

        Reservation updatedReservation = new Reservation(
                reservation != null ? reservation.getId() : null, clientId, roomId, checkInDate, checkOutDate, status);

        reservationApi.createReservation(updatedReservation).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Reserva creada correctamente", Toast.LENGTH_SHORT).show();
                    dismiss();
                    onReservationUpdatedListener.onReservationUpdated(updatedReservation);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface OnReservationUpdatedListener {
        void onReservationUpdated(Reservation updatedReservation);
    }
}