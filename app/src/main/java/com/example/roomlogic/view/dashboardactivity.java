package com.example.roomlogic.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomlogic.R;
import com.example.roomlogic.adapter.ReservationAdapter;
import com.example.roomlogic.model.Reservation;
import com.example.roomlogic.viewmodel.dashboardviewmodel;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

import java.text.SimpleDateFormat; // Importar SimpleDateFormat
import java.util.Date;             // Importar Date
import java.util.Locale;           // Importar Locale
import android.os.Environment;     // Importar para guardar en descargas
import android.widget.Toast;       // Para mostrar mensajes

import java.util.ArrayList;
import java.util.List;

public class dashboardactivity extends AppCompatActivity {

    private dashboardviewmodel dashboardViewModel;
    private ReservationAdapter adapter;
    private List<Reservation> reservations;

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

        // Crear adaptador con una lista vacía y contexto
        List<Reservation> initialReservations = new ArrayList<>();
        adapter = new ReservationAdapter(initialReservations, this);
        rvReservations.setAdapter(adapter);

        // Configurar ViewModel
        dashboardViewModel = new ViewModelProvider(this).get(dashboardviewmodel.class);
        dashboardViewModel.getReservations().observe(this, reservations -> {
            if (reservations != null && !reservations.isEmpty()) {
                this.reservations = reservations;
                adapter.submitList(reservations); // Cargar datos en el RecyclerView
            } else {
                Toast.makeText(this, "No hay reservas para el día de hoy", Toast.LENGTH_SHORT).show();
            }
        });

        // Cargar reservas del día actual
        dashboardViewModel.loadReservations();

        // Configurar botones
        setupButtons();
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
            try {
                generatePdf(reservations); // Llama al método para generar el PDF
                Toast.makeText(this, "PDF generado con éxito", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al generar el PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnImages.setOnClickListener(v -> {
            Toast.makeText(this, "Imágenes", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ImagesActivity.class)); // Ruta a ImagesActivity
        });
    }

    private void generatePdf(List<Reservation> reservations) throws Exception {
        if (reservations == null || reservations.isEmpty()) {
            throw new Exception("No hay datos para generar el PDF");
        }

        // Obtener la fecha y hora actual
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

        // Generar el nombre del archivo con la fecha y hora
        String fileName = "Reservas_" + timestamp + ".pdf";

        // Ruta donde se almacenará el PDF
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + fileName;

        try {
            PdfWriter writer = new PdfWriter(path);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            // Título del documento
            document.add(new Paragraph("RoomLogic")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(40));
            document.add(new Paragraph("Clientes con reservas al día de hoy")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(20));

            // Espaciado
            document.add(new Paragraph("\n"));

            // Iterar las reservas y agregarlas al PDF
            for (Reservation reservation : reservations) {
                document.add(new Paragraph("Nombre: " + reservation.getGuestName())
                        .setFontSize(12));
                document.add(new Paragraph("Teléfono: " + reservation.getPhone())
                        .setFontSize(12));
                document.add(new Paragraph("No. Habitación: " + reservation.getRoomNumber())
                        .setFontSize(12));
                document.add(new Paragraph("Estado: " + reservation.getStatus())
                        .setFontSize(12));
                document.add(new Paragraph("\n")); // Espaciado entre reservas
            }

            // Cerrar el documento
            document.close();

            // Confirmación al usuario
            Toast.makeText(this, "PDF generado con éxito: " + fileName, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // Manejar errores y mostrar un mensaje
            Toast.makeText(this, "Error al generar el PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
