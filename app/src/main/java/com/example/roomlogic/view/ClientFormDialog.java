package com.example.roomlogic.view;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.roomlogic.R;
import com.example.roomlogic.api.ApiClient;
import com.example.roomlogic.api.ClientApi;
import com.example.roomlogic.model.Client;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientFormDialog extends Dialog {
    private EditText etName, etEmail, etPhone, etAddress;
    private Button btnSave;
    private OnClientUpdatedListener listener;
    private ClientApi clientApi; // Agregamos esta variable

    public ClientFormDialog(Context context, Client client, OnClientUpdatedListener listener) {
        super(context);
        this.listener = listener;
        setContentView(R.layout.dialog_client_form);

        etName = findViewById(R.id.etClientName);
        etEmail = findViewById(R.id.etClientEmail);
        etPhone = findViewById(R.id.etClientPhone);
        etAddress = findViewById(R.id.etClientAddress);
        btnSave = findViewById(R.id.btnSaveClient);

        clientApi = ApiClient.getRetrofitInstance().create(ClientApi.class); // Inicializamos `clientApi`

        if (client != null) {
            etName.setText(client.getName());
            etEmail.setText(client.getEmail());
            etPhone.setText(client.getPhone());
            etAddress.setText(client.getAddress());
        }

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String address = etAddress.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(context, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            Client newClient = new Client(client != null ? client.getId() : null, name, email, phone, address, "Active");

            if (client == null) {
                // Crear un nuevo cliente
                clientApi.createClient(newClient).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "Cliente registrado exitosamente", Toast.LENGTH_SHORT).show();
                            listener.onClientUpdated();
                            dismiss();
                        } else {
                            Toast.makeText(context, "Error al registrar el cliente", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // Actualizar un cliente existente
                clientApi.updateClient(client.getId(), newClient).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "Cliente actualizado exitosamente", Toast.LENGTH_SHORT).show();
                            listener.onClientUpdated();
                            dismiss();
                        } else {
                            Toast.makeText(context, "Error al actualizar el cliente", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public interface OnClientUpdatedListener {
        void onClientUpdated();
    }
}
