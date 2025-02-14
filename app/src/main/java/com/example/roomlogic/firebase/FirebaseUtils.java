package com.example.roomlogic.firebase;

import android.util.Log;
import com.example.roomlogic.network.ApiClient;
import com.example.roomlogic.network.ApiService;
import com.example.roomlogic.network.TokenRequest;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebaseUtils {
    private static final String TAG = "FirebaseUtils";

    public static void obtenerTokenFCM() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "No se pudo obtener el Token de FCM", task.getException());
                        return;
                    }

                    // Token obtenido
                    String token = task.getResult();
                    Log.d(TAG, "Token de FCM obtenido: " + token);

                    // Enviar el token a la API
                    enviarTokenFCM(token);
                });
    }

    private static void enviarTokenFCM(String token) {
        ApiService apiService = ApiClient.getApiService();

        // Agregar title y body en la solicitud
        TokenRequest tokenRequest = new TokenRequest(
                "Notificación de Prueba",
                "Tu API Actix está enviando push correctamente!",
                token
        );

        Log.d(TAG, "Enviando JSON a la API: " + new Gson().toJson(tokenRequest));

        apiService.sendToken(tokenRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Notificación enviada con éxito desde la API");
                } else {
                    try {
                        Log.e(TAG, "Error en la respuesta: " + response.code());
                        Log.e(TAG, "Cuerpo de respuesta: " + response.errorBody().string());
                    } catch (IOException e) {
                        Log.e(TAG, "Error al leer el cuerpo de la respuesta", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Error al conectar con la API: " + t.getMessage());
            }
        });
    }
}