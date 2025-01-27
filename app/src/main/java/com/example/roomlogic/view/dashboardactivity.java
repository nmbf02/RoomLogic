package com.example.roomlogic.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomlogic.R;
import com.example.roomlogic.adapter.ReservationAdapter;
import com.example.roomlogic.viewmodel.dashboardviewmodel;

public class dashboardactivity extends AppCompatActivity {
    private dashboardviewmodel dashboardViewModel;
    private ReservationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Configurar RecyclerView
        RecyclerView rvReservations = findViewById(R.id.rvReservations);
        rvReservations.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ReservationAdapter(); // Inicializar el adaptador
        rvReservations.setAdapter(adapter);

        // Configurar ViewModel
        dashboardViewModel = new ViewModelProvider(this).get(dashboardviewmodel.class);
        dashboardViewModel.getReservations().observe(this, reservations -> {
            if (reservations != null && !reservations.isEmpty()) {
                adapter.submitList(reservations); // Poblar el RecyclerView con los datos
            } else {
                Toast.makeText(this, "No hay reservas para el día de hoy", Toast.LENGTH_SHORT).show();
            }
        });

        // Configurar el botón de imágenes
        findViewById(R.id.btnImages).setOnClickListener(v -> {
            // Abrir la actividad ImagesActivity
            startActivity(new Intent(this, ImagesActivity.class));
        });
    }

}