package com.example.roomlogic.view;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomlogic.R;
import com.example.roomlogic.adapter.ReservationAdapter;
import com.example.roomlogic.viewmodel.dashboardviewmodel;

import android.content.Intent;
import android.widget.Button;

public class dashboardactivity extends AppCompatActivity {

    private dashboardviewmodel dashboardViewModel;
    private ReservationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Configurar el título
        TextView tvDashboardTitle = findViewById(R.id.tvDashboardTitle);
        tvDashboardTitle.setText("Clientes con reservas al día de hoy");

        // Configurar RecyclerView
        RecyclerView rvReservations = findViewById(R.id.rvReservations);
        rvReservations.setLayoutManager(new LinearLayoutManager(this));

        // Crear adaptador vacío inicialmente
        adapter = new ReservationAdapter();
        rvReservations.setAdapter(adapter);

        // Configurar ViewModel
        dashboardViewModel = new ViewModelProvider(this).get(dashboardviewmodel.class);
        dashboardViewModel.getReservations().observe(this, reservations -> {
            if (reservations != null && !reservations.isEmpty()) {
                adapter.submitList(reservations); // Actualiza el RecyclerView con las reservas
            } else {
                Toast.makeText(this, "No hay reservas para el día de hoy", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupButtons() {
        Button btnRegister = findViewById(R.id.btnRegister);
        Button btnConsult = findViewById(R.id.btnConsult);
        Button btnReport = findViewById(R.id.btnReport);
        Button btnImages = findViewById(R.id.btnImages);

        btnRegister.setOnClickListener(v -> {
            Toast.makeText(this, "Registrar", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, RegisterActivity.class)); // Ruta a RegisterActivity
        });

        btnConsult.setOnClickListener(v -> {
            Toast.makeText(this, "Consultar", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ConsultActivity.class)); // Ruta a ConsultActivity
        });

        btnReport.setOnClickListener(v -> {
            Toast.makeText(this, "Generar Reporte", Toast.LENGTH_SHORT).show();
        });

        btnImages.setOnClickListener(v -> {
            Toast.makeText(this, "Imágenes", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ImagesActivity.class)); // Ruta a ImagesActivity
        });
    }

}