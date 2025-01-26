package com.example.roomlogic.view;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.roomlogic.R;
import com.example.roomlogic.adapter.ReservationAdapter;
import com.example.roomlogic.model.Reservation;
import com.example.roomlogic.repository.ReservationRepository;
import java.util.ArrayList;
import java.util.List;

public class ReservationsActivity extends AppCompatActivity {

    private RecyclerView recyclerView; // RecyclerView para mostrar las reservas
    private ReservationAdapter reservationAdapter; // Adaptador para el RecyclerView
    private List<Reservation> reservationList = new ArrayList<>(); // Lista de reservas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations);

        // Configuración del RecyclerView
        recyclerView = findViewById(R.id.rvReservations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicialización del adaptador con la lista de reservas y el contexto
        reservationAdapter = new ReservationAdapter(reservationList, this);
        recyclerView.setAdapter(reservationAdapter);

        // Cargar las reservas
        fetchReservations();
    }

    // Método para obtener las reservas del repositorio
    private void fetchReservations() {
        ReservationRepository.getInstance().getReservations(new ReservationRepository.ReservationCallback() {
            @Override
            public void onComplete(List<Reservation> reservationsList) {
                // Actualizar la lista de reservas y notificar al adaptador
                reservationList.clear();
                reservationList.addAll(reservationsList);
                reservationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                // Mostrar un mensaje de error al usuario
                Toast.makeText(ReservationsActivity.this, "Error al cargar reservas: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
