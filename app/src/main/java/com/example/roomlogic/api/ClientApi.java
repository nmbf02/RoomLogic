package com.example.roomlogic.api;

import com.example.roomlogic.model.Client;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ClientApi {
    @GET("/api/clients")
    Call<List<Client>> getClients();

    @POST("/api/clients")
    Call<Void> createClient(@Body Client client);

    @DELETE("/api/clients/{id}")
    Call<Void> deleteClient(@Path("id") int clientId);

    @PUT("/api/clients/{id}")
    Call<Void> updateClient(@Path("id") int clientId, @Body Client client);
}