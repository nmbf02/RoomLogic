package com.example.roomlogic.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.roomlogic.R;
import com.example.roomlogic.api.ApiClient;
import com.example.roomlogic.api.ClientApi;
import com.example.roomlogic.model.Client;
import com.example.roomlogic.view.ManageClientsActivity;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientViewHolder> {
    private List<Client> clientList;
    private Context context;
    private ClientApi clientApi;

    public ClientAdapter(List<Client> clientList, Context context) {
        this.clientList = clientList;
        this.context = context;
        this.clientApi = ApiClient.getClientApi();
    }

    @NonNull
    @Override
    public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_client, parent, false);
        return new ClientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientViewHolder holder, int position) {
        Client client = clientList.get(position);
        holder.tvName.setText(client.getName());
        holder.tvEmail.setText(client.getEmail());

        holder.btnEdit.setOnClickListener(v -> {
            if (context instanceof ManageClientsActivity) {
                ((ManageClientsActivity) context).showClientForm(client);
            }
        });

        holder.btnDelete.setOnClickListener(v -> showDeleteConfirmation(client));
    }

    @Override
    public int getItemCount() {
        return clientList.size();
    }

    private void showDeleteConfirmation(Client client) {
        new AlertDialog.Builder(context)
                .setTitle("Eliminar Cliente")
                .setMessage("¿Seguro que deseas eliminar a " + client.getName() + "?")
                .setPositiveButton("Sí", (dialog, which) -> deleteClient(client))
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteClient(Client client) {
        clientApi.deleteClient(client.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    clientList.remove(client);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Cliente eliminado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al eliminar cliente", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    static class ClientViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail;
        Button btnEdit, btnDelete;

        ClientViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvClientName);
            tvEmail = itemView.findViewById(R.id.tvClientEmail);
            btnEdit = itemView.findViewById(R.id.btnEditClient);
            btnDelete = itemView.findViewById(R.id.btnDeleteClient);
        }
    }
}