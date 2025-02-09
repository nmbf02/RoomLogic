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
import com.example.roomlogic.model.Reservation;
import com.example.roomlogic.api.ApiClient;
import com.example.roomlogic.api.ReservationApi;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {
    private List<Reservation> reservationList;
    private Context context;
    private ReservationApi reservationApi;

    public ReservationAdapter(List<Reservation> reservationList, Context context) {
        this.reservationList = reservationList;
        this.context = context;
        this.reservationApi = ApiClient.getRetrofitInstance().create(ReservationApi.class);
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);

        holder.tvReservationId.setText("Reserva ID: " + (reservation.getId() != null ? reservation.getId() : "Sin ID"));
        holder.tvGuestId.setText("Guest ID: " + reservation.getGuestId());
        holder.tvRoomId.setText("Room ID: " + reservation.getRoomId());
        holder.tvCheckInDate.setText("Check-In: " + (reservation.getCheckInDate() != null ? reservation.getCheckInDate() : "No definido"));
        holder.tvCheckOutDate.setText("Check-Out: " + (reservation.getCheckOutDate() != null ? reservation.getCheckOutDate() : "No definido"));
        holder.tvReservationStatus.setText("Status: " + reservation.getStatus());

        holder.btnEdit.setOnClickListener(v -> {
            // Aquí puedes implementar la funcionalidad para editar la reserva
            Toast.makeText(context, "Editar reserva ID: " + reservation.getId(), Toast.LENGTH_SHORT).show();
        });

        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Eliminar Reserva")
                    .setMessage("¿Deseas eliminar esta reserva?")
                    .setPositiveButton("Sí", (dialog, which) -> deleteReservation(reservation, position))
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    public void updateReservations(List<Reservation> newReservations) {
        this.reservationList.clear();
        this.reservationList.addAll(newReservations);
        notifyDataSetChanged();
    }

    private void deleteReservation(Reservation reservation, int position) {
        reservationApi.deleteReservation(reservation.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    reservationList.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Reserva eliminada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al eliminar reserva", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView tvReservationId, tvGuestId, tvRoomId, tvCheckInDate, tvCheckOutDate, tvReservationStatus;
        Button btnEdit, btnDelete;

        ReservationViewHolder(View itemView) {
            super(itemView);
            tvReservationId = itemView.findViewById(R.id.tvReservationId); // ✅ Agregada la vista del ID
            tvGuestId = itemView.findViewById(R.id.tvGuestId);
            tvRoomId = itemView.findViewById(R.id.tvRoomId);
            tvCheckInDate = itemView.findViewById(R.id.tvCheckInDate);
            tvCheckOutDate = itemView.findViewById(R.id.tvCheckOutDate);
            tvReservationStatus = itemView.findViewById(R.id.tvReservationStatus);
            btnEdit = itemView.findViewById(R.id.btnEditReservation); // ✅ Agregado botón de editar
            btnDelete = itemView.findViewById(R.id.btnDeleteReservation);
        }
    }
}