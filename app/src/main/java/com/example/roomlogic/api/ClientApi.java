package com.example.roomlogic.api;

import com.example.roomlogic.model.Client;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.DELETE;
import retrofit2.http.Path;

public interface ClientApi {
    @GET("clients") // Endpoint de la API para obtener clientes
    Call<List<Client>> getClients();

    @DELETE("clients/{id}")
    Call<Void> deleteClient(@Path("id") int clientId);
}
