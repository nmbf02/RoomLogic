package com.example.roomlogic.viewmodel;

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

    public LiveData<List<Reservation>> getReservationsForToday() {
        return reservationsLiveData;
    }

    private void fetchReservationsForToday() {
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        reservationApi.getReservationsForToday().enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reservationsLiveData.setValue(response.body());
                } else {
                    reservationsLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {
                reservationsLiveData.setValue(null);
            }
        });
    }
}