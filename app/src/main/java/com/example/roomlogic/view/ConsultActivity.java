package com.example.roomlogic.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.roomlogic.R;

public class ConsultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult);

        //Consular Clientes
        Button btnClientes = findViewById(R.id.btnClientes);
        btnClientes.setOnClickListener(v -> {
            Intent intent = new Intent(ConsultActivity.this, ClientsActivity.class);
            startActivity(intent);
        });

        // Consultar Habitaciones
        Button btnHabitaciones = findViewById(R.id.btnHabitaciones);
        btnHabitaciones.setOnClickListener(v -> {
            Intent intent = new Intent(ConsultActivity.this, RoomsActivity.class);
            startActivity(intent);
        });

        // Configurar el botón "Reservas al día de hoy"
        Button btnReservasHoy = findViewById(R.id.btnVolver);
        btnReservasHoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar a dashboardactivity
                Intent intent = new Intent(ConsultActivity.this, dashboardactivity.class);
                startActivity(intent);
                finish(); // Opcional: cerrar esta actividad si no quieres que el usuario regrese a ella
            }
        });
    }
}
