package com.example.roomlogic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.roomlogic.R;
import com.example.roomlogic.model.Room;
import com.example.roomlogic.repository.RoomRepository;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private final List<Room> roomList;
    private final Context context;

    // Constructor
    public RoomAdapter(List<Room> roomList, Context context) {
        this.roomList = roomList;
        this.context = context;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);

        holder.tvRoomNumber.setText(room.getRoomNumber());
        holder.tvType.setText(room.getRoomType());
        holder.tvPrice.setText(String.format("$%.2f", room.getPricePerNight()));
        holder.tvStatus.setText(room.getStatus());

        holder.ivDeleteRoom.setOnClickListener(v -> {
            deleteRoom(room.getId(), position);
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    // Método para eliminar una habitación
    private void deleteRoom(int roomId, int position) {
        RoomRepository.getInstance().deleteRoom(roomId, new RoomRepository.DeleteRoomCallback() {
            @Override
            public void onSuccess() {
                roomList.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(context, "Habitación eliminada", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(context, "Error al eliminar la habitación", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ViewHolder
    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomNumber, tvType, tvPrice, tvStatus;
        ImageView ivDeleteRoom;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomNumber = itemView.findViewById(R.id.tvRoomNumber);
            tvType = itemView.findViewById(R.id.tvType);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            ivDeleteRoom = itemView.findViewById(R.id.ivDeleteRoom); // Icono de eliminar
        }
    }
}