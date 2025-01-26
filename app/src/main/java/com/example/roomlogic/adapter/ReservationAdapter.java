package com.example.roomlogic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.roomlogic.R;
import com.example.roomlogic.model.Reservation;
import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder> {

    private List<Reservation> reservationList;

    public ReservationAdapter(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    public void updateList(List<Reservation> newReservations) {
        this.reservationList = newReservations;
        notifyDataSetChanged(); // Notifica al RecyclerView sobre los cambios
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reservation, parent, false); // Asegúrate de que el layout XML sea correcto
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);

        // Configura los datos en las vistas
        holder.tvGuestName.setText(reservation.getGuestName());
        holder.tvPhone.setText(reservation.getPhone());
        holder.tvRoomNumber.setText(String.valueOf(reservation.getRoomNumber()));
        holder.tvStatus.setText(reservation.getStatus());
    }

    @Override
    public int getItemCount() {
        return reservationList != null ? reservationList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvGuestName, tvPhone, tvRoomNumber, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Vincula las vistas con los IDs en el layout XML
            tvGuestName = itemView.findViewById(R.id.tvGuestNameValue);
            tvPhone = itemView.findViewById(R.id.tvPhoneValue);
            tvRoomNumber = itemView.findViewById(R.id.tvRoomNumberValue);
            tvStatus = itemView.findViewById(R.id.tvStatusValue);
        }
    }
}
