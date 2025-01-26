package com.example.roomlogic.view;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.roomlogic.R;
import com.example.roomlogic.adapter.ClientAdapter;
import com.example.roomlogic.model.Client;
import com.example.roomlogic.repository.ClientRepository;

import java.util.ArrayList;
import java.util.List;

public class ClientsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ClientAdapter clientAdapter;
    private List<Client> clientList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);

        recyclerView = findViewById(R.id.rvClients);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        clientAdapter = new ClientAdapter(clientList, this);
        recyclerView.setAdapter(clientAdapter);

        fetchClients();
    }

    private void fetchClients() {
        ClientRepository.getInstance().fetchClients(new ClientRepository.ClientCallback() {
            @Override
            public void onSuccess(List<Client> clients) {
                clientList.clear();
                clientList.addAll(clients);
                clientAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(ClientsActivity.this, "Error al cargar clientes", Toast.LENGTH_SHORT).show();
            }
        });
    }
}