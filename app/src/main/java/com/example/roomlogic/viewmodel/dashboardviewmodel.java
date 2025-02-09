package com.example.roomlogic.viewmodel;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.roomlogic.api.ApiClient;
import com.example.roomlogic.api.ReservationApi;
import com.example.roomlogic.model.Reservation;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class dashboardviewmodel extends ViewModel {
    private MutableLiveData<List<Reservation>> reservationsLiveData = new MutableLiveData<>();
    private ReservationApi reservationApi;

    public dashboardviewmodel() {
        reservationApi = ApiClient.getRetrofitInstance().create(ReservationApi.class);
        fetchReservationsForToday();
    }

    /**
     * Método para obtener las reservas del día actual.
     * @return LiveData con la lista de reservas.
     */
    public LiveData<List<Reservation>> getReservationsForToday() {
        return reservationsLiveData;
    }

    /**
     * Método para obtener la fecha actual en formato YYYY-MM-DD.
     * @return Fecha actual como String.
     */
    private String getTodayDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    /**
     * Método que realiza la petición a la API para obtener las reservas del día actual.
     */
    private void fetchReservationsForToday() {
        String todayDate = getTodayDate();
        Log.d("VIEWMODEL_DEBUG", "Obteniendo reservas para la fecha: " + todayDate);

        reservationApi.getReservationsForToday().enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reservationsLiveData.setValue(response.body());
                    Log.d("VIEWMODEL_DEBUG", "Reservas recibidas: " + response.body().size());
                } else {
                    Log.w("VIEWMODEL_WARNING", "No hay reservas disponibles para hoy");
                    reservationsLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {
                Log.e("VIEWMODEL_ERROR", "Error al obtener reservas: " + t.getMessage());
                reservationsLiveData.setValue(null);
            }
        });
    }
}