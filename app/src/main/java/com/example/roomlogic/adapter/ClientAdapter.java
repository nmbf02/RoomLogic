package com.example.roomlogic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomlogic.R;
import com.example.roomlogic.model.Client;
import com.example.roomlogic.repository.ClientRepository;

import java.util.List;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientViewHolder> {

    private final List<Client> clientList;
    private final Context context;

    public ClientAdapter(List<Client> clientList, Context context) {
        this.clientList = clientList;
        this.context = context;
    }

    @NonNull
    @Override
    public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_client, parent, false);
        return new ClientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientViewHolder holder, int position) {
        Client client = clientList.get(position);

        holder.tvName.setText(client.getName());
        holder.tvEmail.setText(client.getEmail());
        holder.tvPhone.setText(client.getPhone());

        holder.ivDeleteClient.setOnClickListener(v -> deleteClient(client.getId(), position));
    }

    @Override
    public int getItemCount() {
        return clientList.size();
    }

    private void deleteClient(int clientId, int position) {
        ClientRepository.getInstance().deleteClient(clientId, new ClientRepository.DeleteClientCallback() {
            @Override
            public void onSuccess() {
                clientList.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(context, "Cliente eliminado con éxito", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(context, "Error al eliminar cliente", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class ClientViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail, tvPhone;
        ImageView ivDeleteClient;

        public ClientViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            ivDeleteClient = itemView.findViewById(R.id.ivDeleteClient);
        }
    }
}