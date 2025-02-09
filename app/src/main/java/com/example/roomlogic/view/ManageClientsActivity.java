package com.example.roomlogic.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.roomlogic.R;
import com.example.roomlogic.adapter.ClientAdapter;
import com.example.roomlogic.api.ApiClient;
import com.example.roomlogic.api.ClientApi;
import com.example.roomlogic.model.Client;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageClientsActivity extends AppCompatActivity {

    private RecyclerView rvClients;
    private ClientAdapter clientAdapter;
    private ClientApi clientApi;
    private Button btnAddClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_clients);

        rvClients = findViewById(R.id.rvClients);
        rvClients.setLayoutManager(new LinearLayoutManager(this));
        btnAddClient = findViewById(R.id.btnAddClient);

        clientApi = ApiClient.getClientApi();

        loadClients();

        btnAddClient.setOnClickListener(v -> showClientForm(null));
    }

    private void loadClients() {
        clientApi.getClients().enqueue(new Callback<List<Client>>() {
            @Override
            public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    clientAdapter = new ClientAdapter(response.body(), ManageClientsActivity.this);
                    rvClients.setAdapter(clientAdapter);
                } else {
                    Toast.makeText(ManageClientsActivity.this, "Error al cargar clientes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Client>> call, Throwable t) {
                Toast.makeText(ManageClientsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showClientForm(Client client) {
        ClientFormDialog dialog = new ClientFormDialog(this, client, () -> loadClients());
        dialog.show();
    }
}