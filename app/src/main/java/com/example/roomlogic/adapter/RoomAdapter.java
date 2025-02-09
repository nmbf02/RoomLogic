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
import com.example.roomlogic.api.ApiClient;
import com.example.roomlogic.api.RoomApi;
import com.example.roomlogic.model.Room;
import com.example.roomlogic.view.RoomFormDialog;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private List<Room> roomList;
    private Context context;
    private RoomApi roomApi;

    public RoomAdapter(List<Room> roomList, Context context) {
        this.roomList = roomList;
        this.context = context;
        this.roomApi = ApiClient.getRoomApi();
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        holder.tvRoomNumber.setText("Habitación: " + (room.getRoomNumber() != null ? room.getRoomNumber() : "Sin número"));
        holder.tvRoomType.setText("Tipo: " + (room.getRoomType() != null ? room.getRoomType() : "Sin especificar"));
        holder.tvRoomStatus.setText("Estado: " + (room.getStatus() != null ? room.getStatus() : "Desconocido"));

        // Botón de editar habitación
        holder.btnEdit.setOnClickListener(v -> showRoomFormDialog(room, position)); // Ahora pasa correctamente ambos parámetros

        // Botón de eliminar habitación
        holder.btnDelete.setOnClickListener(v -> showDeleteConfirmation(room, position));
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    /**
     * 📌 Mostrar un diálogo de confirmación antes de eliminar la habitación.
     */
    private void showDeleteConfirmation(Room room, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Eliminar Habitación")
                .setMessage("¿Seguro que deseas eliminar la habitación " + room.getRoomNumber() + "?")
                .setPositiveButton("Sí", (dialog, which) -> deleteRoom(room, position)) // Llamar a deleteRoom con posición
                .setNegativeButton("No", null)
                .show();
    }

    /**
     * 📌 Eliminar la habitación de la base de datos.
     */
    private void deleteRoom(Room room, int position) {
        roomApi.deleteRoom(room.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    roomList.remove(position); // Elimina por posición en lugar de `room`
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Habitación eliminada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al eliminar habitación", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 📌 Mostrar el formulario para editar una habitación.
     */
    private void showRoomFormDialog(Room room, int position) {
        RoomFormDialog dialog = new RoomFormDialog(context, room, updatedRoom -> {
            // Actualiza la lista con la habitación editada
            roomList.set(position, updatedRoom);
            notifyItemChanged(position);
        });
        dialog.show();
    }

    static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomNumber, tvRoomType, tvRoomStatus;
        Button btnEdit, btnDelete;

        RoomViewHolder(View itemView) {
            super(itemView);
            tvRoomNumber = itemView.findViewById(R.id.tvRoomNumber);
            tvRoomType = itemView.findViewById(R.id.tvRoomType);
            tvRoomStatus = itemView.findViewById(R.id.tvRoomStatus);
            btnEdit = itemView.findViewById(R.id.btnEditRoom);
            btnDelete = itemView.findViewById(R.id.btnDeleteRoom);
        }
    }
}