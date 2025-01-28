package com.example.roomlogic.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.roomlogic.R;
import com.example.roomlogic.api.ApiClient;
import com.example.roomlogic.api.ApiService;
import com.example.roomlogic.model.Client;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        ApiService apiService = ApiClient.getApiService(); // Instancia de Retrofit

        btnSaveClient.setOnClickListener(v -> {
            String name = etClientName.getText().toString().trim();
            String email = etClientEmail.getText().toString().trim();
            String phone = etClientPhone.getText().toString().trim();
            String address = etClientAddress.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Crear el objeto Client
            Client client = new Client(null, name, email, phone, address, "Active");

            // Llamar al endpoint para registrar el cliente
            apiService.createClient(client).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(RegisterClientActivity.this, "Cliente registrado exitosamente", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            Toast.makeText(RegisterClientActivity.this, "Error del servidor: " + errorBody, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(RegisterClientActivity.this, "Error desconocido", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(RegisterClientActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        });
    }
}
