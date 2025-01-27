package com.example.roomlogic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.example.roomlogic.R;
import com.example.roomlogic.model.Reservation;

public class ReservationAdapter extends ListAdapter<Reservation, ReservationAdapter.ReservationViewHolder> {

    public ReservationAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Reservation> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Reservation>() {
                @Override
                public boolean areItemsTheSame(@NonNull Reservation oldItem, @NonNull Reservation newItem) {
                    return oldItem.getRoomNumber() == newItem.getRoomNumber();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Reservation oldItem, @NonNull Reservation newItem) {
                    return oldItem.equals(newItem);
                }
            };

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reservation, parent, false);
        return new ReservationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = getItem(position);
        holder.bind(reservation);
    }

    static class ReservationViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvRoomType;
        private final TextView tvPhone;
        private final TextView tvRoomNumber;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvRoomType = itemView.findViewById(R.id.tvRoomType);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvRoomNumber = itemView.findViewById(R.id.tvRoomNumber);
        }

        public void bind(Reservation reservation) {
            tvName.setText(reservation.getName());
            tvRoomType.setText(reservation.getRoomType());
            tvPhone.setText(reservation.getPhone());
            tvRoomNumber.setText(String.valueOf(reservation.getRoomNumber()));
        }
    }
}
