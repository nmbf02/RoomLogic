package com.example.roomlogic.repository;

import com.example.roomlogic.api.ClientApi;
import com.example.roomlogic.model.Client;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientRepository {
    private static ClientRepository instance;
    private final ClientApi clientApi;

    private ClientRepository(ClientApi clientApi) {
        this.clientApi = clientApi;
    }

    public static synchronized ClientRepository getInstance() {
        if (instance == null) {
            instance = new ClientRepository(RetrofitClient.getInstance().create(ClientApi.class));
        }
        return instance;
    }

    public void fetchClients(ClientCallback callback) {
        Call<List<Client>> call = clientApi.getClients();
        call.enqueue(new Callback<List<Client>>() {
            @Override
            public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error al obtener los clientes");
                }
            }

            @Override
            public void onFailure(Call<List<Client>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void deleteClient(int clientId, DeleteClientCallback callback) {
        Call<Void> call = clientApi.deleteClient(clientId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError("Error al eliminar cliente");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    // Callback para eliminación
    public interface DeleteClientCallback {
        void onSuccess();
        void onError(String error);
    }


    public interface ClientCallback {
        void onSuccess(List<Client> clients);
        void onError(String error);
    }
}