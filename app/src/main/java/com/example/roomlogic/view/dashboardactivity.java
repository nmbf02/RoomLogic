package com.example.roomlogic.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.roomlogic.R;
import com.example.roomlogic.adapter.ReservationAdapter;
import com.example.roomlogic.api.ApiClient;
import com.example.roomlogic.api.ReservationApi;
import com.example.roomlogic.model.Reservation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class dashboardactivity extends AppCompatActivity {
    private ReservationAdapter adapter;
    private ReservationApi reservationApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Configurar RecyclerView
        RecyclerView rvReservations = findViewById(R.id.rvReservations);
        rvReservations.setLayoutManager(new LinearLayoutManager(this));

        // ✅ Inicializar adaptador con una lista vacía y el contexto
        adapter = new ReservationAdapter(new ArrayList<>(), this);
        rvReservations.setAdapter(adapter);

        // Instanciar API
        reservationApi = ApiClient.getRetrofitInstance().create(ReservationApi.class);

        // Cargar reservas del día actual
        loadReservationsForToday();

        // Configurar el botón de imágenes
        findViewById(R.id.btnImages).setOnClickListener(v -> {
            startActivity(new Intent(this, ImagesActivity.class));
        });

        // Configurar el botón "Registrar"
        findViewById(R.id.btnRegister).setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // ✅ Forzar la actualización de reservas cuando el usuario regresa a esta pantalla
        loadReservationsForToday();
    }

    // ✅ Método para cargar reservas del día actual
    private void loadReservationsForToday() {
        // ✅ Obtener la fecha actual en formato YYYY-MM-DD
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String todayDate = sdf.format(new Date());

        Log.d("DASHBOARD_DEBUG", "Cargando reservas para la fecha: " + todayDate);

        reservationApi.getReservationsForToday().enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Reservation> reservations = response.body();
                    Log.d("DASHBOARD_DEBUG", "Reservas recibidas: " + reservations.size());

                    adapter.updateReservations(reservations);
                } else {
                    Log.w("DASHBOARD_WARNING", "No hay reservas para hoy");
                    Toast.makeText(dashboardactivity.this, "No hay reservas para hoy", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {
                Log.e("DASHBOARD_ERROR", "Error al obtener reservas: " + t.getMessage());
                Toast.makeText(dashboardactivity.this, "Error al obtener reservas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}