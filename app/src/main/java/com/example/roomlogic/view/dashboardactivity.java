package com.example.roomlogic.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.roomlogic.R;
import com.example.roomlogic.adapter.ReservationAdapter;
import com.example.roomlogic.model.Reservation;
import com.example.roomlogic.viewmodel.dashboardviewmodel;

import java.util.ArrayList;
import java.util.List;

import android.graphics.pdf.PdfDocument;
import android.graphics.Paint;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.os.Environment;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class dashboardactivity extends AppCompatActivity {
    private ReservationAdapter adapter;
    private dashboardviewmodel viewModel;
    private static final int STORAGE_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Configurar RecyclerView
        RecyclerView rvReservations = findViewById(R.id.rvReservations);
        rvReservations.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar adaptador con lista vacía
        adapter = new ReservationAdapter(new ArrayList<>(), this);
        rvReservations.setAdapter(adapter);

        // Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(dashboardviewmodel.class);

        // Observar los cambios en las reservas del día
        viewModel.getReservationsForToday().observe(this, reservations -> {
            if (reservations != null && !reservations.isEmpty()) {
                adapter.updateReservations(reservations);
                Log.d("DASHBOARD_UI", "Mostrando " + reservations.size() + " reservas.");
            } else {
                Toast.makeText(this, "No hay reservas para hoy", Toast.LENGTH_SHORT).show();
                Log.w("DASHBOARD_UI", "No hay reservas disponibles para mostrar.");
            }
        });

        // Configurar botones
        findViewById(R.id.btnImages).setOnClickListener(v -> {
            startActivity(new Intent(this, ImagesActivity.class));
        });

        findViewById(R.id.btnRegister).setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        findViewById(R.id.btnReport).setOnClickListener(v -> {
            if (checkStoragePermission()) {
                generateReportPdf();
            } else {
                requestStoragePermission();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getReservationsForToday();
    }

    private void generateReportPdf() {
        viewModel.getReservationsForToday().observe(this, reservations -> {
            if (reservations != null && !reservations.isEmpty()) {
                createPdf(reservations);
            } else {
                Toast.makeText(dashboardactivity.this, "No hay reservas para generar el reporte", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createPdf(List<Reservation> reservations) {
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(600, 800, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Paint paint = new Paint();
        int yPosition = 50;
        int xPosition = 20;

        paint.setTextSize(18);
        page.getCanvas().drawText("Reporte de Reservas", xPosition, yPosition, paint);
        paint.setTextSize(12);
        yPosition += 30;

        for (Reservation reservation : reservations) {
            page.getCanvas().drawText("ID: " + reservation.getId(), xPosition, yPosition, paint);
            yPosition += 20;
            page.getCanvas().drawText("Cliente ID: " + reservation.getGuestId(), xPosition, yPosition, paint);
            yPosition += 20;
            page.getCanvas().drawText("Habitación ID: " + reservation.getRoomId(), xPosition, yPosition, paint);
            yPosition += 20;
            page.getCanvas().drawText("Check-in: " + reservation.getCheckInDate(), xPosition, yPosition, paint);
            yPosition += 20;
            page.getCanvas().drawText("Check-out: " + reservation.getCheckOutDate(), xPosition, yPosition, paint);
            yPosition += 20;
            page.getCanvas().drawText("Estado: " + reservation.getStatus(), xPosition, yPosition, paint);
            yPosition += 40;
        }

        pdfDocument.finishPage(page);

        String timeStamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.getDefault()).format(new java.util.Date());
        String fileName = "Reporte_Reservas_" + timeStamp + ".pdf";

        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(directory, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            pdfDocument.writeTo(fos);
            pdfDocument.close();
            fos.close();
            Toast.makeText(this, "Reporte guardado en: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al generar el PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkStoragePermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestStoragePermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
            } catch (Exception e) {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                generateReportPdf();
            } else {
                Toast.makeText(this, "Permiso denegado. No se puede generar el reporte.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
