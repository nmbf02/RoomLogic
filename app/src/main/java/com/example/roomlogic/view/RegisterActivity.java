package com.example.roomlogic.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.roomlogic.R;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViewById(R.id.btnRegisterClient).setOnClickListener(v -> {
            // Abrir la actividad para registrar un cliente
            startActivity(new Intent(this, ManageClientsActivity.class));
        });

        findViewById(R.id.btnRegisterRoom).setOnClickListener(v -> {
            // Abrir la actividad para registrar una habitación
            startActivity(new Intent(this, ManageRoomsActivity.class));
        });
    }
}
