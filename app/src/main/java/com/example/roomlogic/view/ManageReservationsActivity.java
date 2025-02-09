package com.example.roomlogic.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.roomlogic.R;
import com.example.roomlogic.adapter.ReservationAdapter;
import com.example.roomlogic.api.ApiClient;
import com.example.roomlogic.api.ReservationApi;
import com.example.roomlogic.model.Reservation;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;

public class ManageReservationsActivity extends AppCompatActivity {
    private RecyclerView recyclerViewReservations;
    private ReservationAdapter reservationAdapter;
    private ReservationApi reservationApi;
    private List<Reservation> reservationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_reservations);

        recyclerViewReservations = findViewById(R.id.recyclerViewReservations);
        recyclerViewReservations.setLayoutManager(new LinearLayoutManager(this));

        reservationApi = ApiClient.getRetrofitInstance().create(ReservationApi.class);

        // Botón para agregar una reserva
        Button btnAddReservation = findViewById(R.id.btnAddReservation);
        btnAddReservation.setOnClickListener(v -> {
            ReservationFormDialog dialog = new ReservationFormDialog(this, null, new ReservationFormDialog.OnReservationUpdatedListener() {
                @Override
                public void onReservationUpdated(Reservation updatedReservation) {
                    loadReservations();
                }
            });
            dialog.show();
        });

        // Inicializar adaptador
        reservationAdapter = new ReservationAdapter(reservationList, this);
        recyclerViewReservations.setAdapter(reservationAdapter);

        // Cargar reservas
        loadReservations();
    }

    private void loadReservations() {
        reservationApi.getReservations().enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("RESERVATION_DEBUG", "Reservas recibidas: " + response.body().size());
                    reservationList.clear();
                    reservationList.addAll(response.body());
                    reservationAdapter.notifyDataSetChanged();
                } else {
                    Log.e("RESERVATION_DEBUG", "Error en la respuesta: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {
                Toast.makeText(ManageReservationsActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}