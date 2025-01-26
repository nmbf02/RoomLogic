package com.example.roomlogic.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.roomlogic.R;
import com.example.roomlogic.viewmodel.loginviewmodel;

public class loginactivity extends AppCompatActivity {

    private loginviewmodel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar vistas
        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);

        // Configurar ViewModel
        loginViewModel = new ViewModelProvider(this).get(loginviewmodel.class);

        // Configurar acción del botón de login
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Validar campos vacíos
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Realizar la solicitud de login
            loginViewModel.login(username, password).observe(this, response -> {
                if (response.isSuccess()) {
                    Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();

                    // Redirigir al Dashboard
                    Intent intent = new Intent(loginactivity.this, dashboardactivity.class);
                    startActivity(intent);
                    finish(); // Cierra LoginActivity
                } else {
                    Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}