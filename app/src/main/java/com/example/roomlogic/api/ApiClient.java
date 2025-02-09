package com.example.roomlogic.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // URL base de la API
    private static final String BASE_URL = "http://10.0.2.2:8080"; // Para emulador, cambia si usas un dispositivo físico
    private static Retrofit retrofit;

    // Método para obtener la instancia de Retrofit
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // Define la URL base de tu API
                    .addConverterFactory(GsonConverterFactory.create()) // Convierte JSON automáticamente
                    .build();
        }
        return retrofit;
    }

    // Método para obtener la instancia de ApiService
    public static ApiService getApiService() {
        return getRetrofitInstance().create(ApiService.class);
    }

    // Método para obtener la instancia de ClientApi
    public static ClientApi getClientApi() {
        return getRetrofitInstance().create(ClientApi.class);
    }
    // Método para obtener la API de Habitaciones
    public static RoomApi getRoomApi() {
        return getRetrofitInstance().create(RoomApi.class);
    }
}
