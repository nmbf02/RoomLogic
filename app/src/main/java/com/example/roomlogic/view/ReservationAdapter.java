package com.example.roomlogic.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.roomlogic.R;
import com.example.roomlogic.model.Reservation;
import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private final List<Reservation> reservations;

    public ReservationAdapter(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservations.get(position);

        holder.tvGuestNameValue.setText(reservation.getGuestName());
        holder.tvPhoneValue.setText(reservation.getPhone());
        holder.tvRoomNumberValue.setText(reservation.getRoomNumber());
        holder.tvStatusValue.setText(reservation.getStatus());
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    // Clase ViewHolder definida correctamente
    static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView tvGuestNameValue, tvPhoneValue, tvRoomNumberValue, tvStatusValue;

        ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGuestNameValue = itemView.findViewById(R.id.tvGuestNameValue);
            tvPhoneValue = itemView.findViewById(R.id.tvPhoneValue);
            tvRoomNumberValue = itemView.findViewById(R.id.tvRoomNumberValue);
            tvStatusValue = itemView.findViewById(R.id.tvStatusValue);
        }
    }
}