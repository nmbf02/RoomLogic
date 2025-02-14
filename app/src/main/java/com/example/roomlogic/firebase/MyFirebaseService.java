package com.example.roomlogic.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import com.example.roomlogic.R;
import com.example.roomlogic.view.dashboardactivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseService";
    private static final String CHANNEL_ID = "roomlogic_notifications";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        try {
            Log.d(TAG, "Mensaje recibido de: " + remoteMessage.getFrom());

            String title = remoteMessage.getNotification() != null ? remoteMessage.getNotification().getTitle() : "Notificación";
            String message = remoteMessage.getNotification() != null ? remoteMessage.getNotification().getBody() : "Mensaje recibido";

            sendNotification(title, message);

        } catch (Exception e) {
            Log.e(TAG, "Error en onMessageReceived: " + e.getMessage(), e);
        }
    }

    private void sendNotification(String title, String message) {
        try {
            // Crear canal de notificación (solo Android 8+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID,
                        "RoomLogic Notifications",
                        NotificationManager.IMPORTANCE_DEFAULT
                );
                NotificationManager manager = getSystemService(NotificationManager.class);
                if (manager != null) {
                    manager.createNotificationChannel(channel);
                }
            }

            // Intent para abrir la app al tocar la notificación
            Intent intent = new Intent(this, dashboardactivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

            // Validar que el ícono de la notificación existe
            int notificationIcon = R.drawable.ic_notification; // Asegúrate de que existe en res/drawable
            if (notificationIcon == 0) {
                Log.e("NOTIFICACIÓN", "No se encontró el ícono de la notificación. Usando el predeterminado.");
                notificationIcon = android.R.drawable.ic_dialog_info; // Ícono por defecto
            }

            // Crear la notificación
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(notificationIcon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            // **Verificar permiso en Android 13+ antes de enviar la notificación**
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    Log.e("NOTIFICACIONES", "No se puede enviar la notificación, permiso denegado.");
                    return;
                }
            }

            // Mostrar la notificación
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(1, builder.build());

        } catch (Exception e) {
            Log.e("NOTIFICACIONES", "Error al enviar la notificación: " + e.getMessage(), e);
        }
    }
}
