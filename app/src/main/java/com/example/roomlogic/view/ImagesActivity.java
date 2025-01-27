package com.example.roomlogic.view;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.roomlogic.R;

public class ImagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        // Configurar botones
        findViewById(R.id.btnUploadImages).setOnClickListener(v -> {
            // Lógica para subir imágenes
            Toast.makeText(this, "Función para subir imágenes", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btnDownloadImages).setOnClickListener(v -> {
            // Lógica para bajar imágenes
            Toast.makeText(this, "Función para bajar imágenes", Toast.LENGTH_SHORT).show();
        });
    }
}
