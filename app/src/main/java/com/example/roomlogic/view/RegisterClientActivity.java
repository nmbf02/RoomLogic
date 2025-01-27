package com.example.roomlogic.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.roomlogic.R;

public class RegisterClientActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client);

        EditText etClientName = findViewById(R.id.etClientName);
        EditText etClientEmail = findViewById(R.id.etClientEmail);
        EditText etClientPhone = findViewById(R.id.etClientPhone);
        EditText etClientAddress = findViewById(R.id.etClientAddress);
        Button btnSaveClient = findViewById(R.id.btnSaveClient);

        btnSaveClient.setOnClickListener(v -> {
            String name = etClientName.getText().toString().trim();
            String email = etClientEmail.getText().toString().trim();
            String phone = etClientPhone.getText().toString().trim();
            String address = etClientAddress.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Aquí puedes agregar lógica para enviar los datos a la base de datos o API
            Toast.makeText(this, "Cliente registrado exitosamente", Toast.LENGTH_SHORT).show();
            finish(); // Cerrar la actividad después de guardar
        });
    }
}
