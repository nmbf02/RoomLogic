package com.example.roomlogic.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.example.roomlogic.R;
import com.example.roomlogic.viewmodel.ImageViewModel;
import android.app.DownloadManager;
import android.content.Context;
import android.os.Environment;

public class ImagesActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 100;

    private ImageViewModel imageViewModel;
    private EditText etSearch;
    private Button btnSearch, btnUploadImages;
    private ImageView imageView;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        // ✅ Solicitar permisos necesarios en tiempo de ejecución
        requestPermissionsIfNecessary();

        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnUploadImages = findViewById(R.id.btnUploadImages);
        imageView = findViewById(R.id.imageView);

        imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);

        // 🔹 Buscar imágenes en Unsplash
        btnSearch.setOnClickListener(v -> {
            String query = etSearch.getText().toString().trim();
            if (!query.isEmpty()) {
                if (isConnected()) {
                    Log.d("ImagesActivity", "Buscando imágenes con query: " + query);
                    imageViewModel.searchImages(query);
                } else {
                    Toast.makeText(this, "Sin conexión a Internet", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Ingrese un término de búsqueda", Toast.LENGTH_SHORT).show();
            }
        });

        // 🔹 Observador para mostrar imágenes obtenidas de Unsplash
        imageViewModel.getImagesLiveData().observe(this, images -> {
            if (images != null && !images.isEmpty()) {
                String imageUrl = images.get(0).getUrls().getRegular();
                Log.d("ImagesActivity", "Imagen obtenida: " + imageUrl);
                Glide.with(this).load(imageUrl).into(imageView);
            } else {
                Log.e("ImagesActivity", "No se encontraron imágenes en Unsplash.");
                Toast.makeText(this, "No se encontraron imágenes", Toast.LENGTH_SHORT).show();
            }
        });

        // 🔹 Subir imágenes a ImgBB
        btnUploadImages.setOnClickListener(v -> openFilePicker());

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {
                            Log.d("ImagesActivity", "Imagen seleccionada para subir: " + imageUri.toString());
                            imageViewModel.uploadImage(imageUri);
                        }
                    }
                });

        // 🔹 Observar imágenes subidas y mostrarlas
        imageViewModel.getUploadedImageUrl().observe(this, url -> {
            if (url != null) {
                Log.d("ImagesActivity", "Imagen subida correctamente: " + url);
                Glide.with(this).load(url).into(imageView);
            } else {
                Log.e("ImagesActivity", "Error al subir la imagen.");
                Toast.makeText(this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnDownloadImages = findViewById(R.id.btnDownloadImages);
        btnDownloadImages.setOnClickListener(v -> {
            String imageUrl = imageViewModel.getImagesLiveData().getValue() != null &&
                    !imageViewModel.getImagesLiveData().getValue().isEmpty() ?
                    imageViewModel.getImagesLiveData().getValue().get(0).getUrls().getRegular() : null;

            downloadImage(imageUrl);
        });
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    // ✅ Corrección para Android 10+ en adelante
    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        if (cm != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
                return capabilities != null && (
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                );
            } else {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            }
        }
        return false;
    }

    // ✅ Solicitar permisos en tiempo de ejecución
    private void requestPermissionsIfNecessary() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, PERMISSION_REQUEST_CODE);
        }
    }

    // ✅ Manejo de la respuesta del usuario al aceptar o denegar permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("ImagesActivity", "Permiso ACCESS_NETWORK_STATE concedido.");
            } else {
                Log.e("ImagesActivity", "Permiso ACCESS_NETWORK_STATE denegado.");
                Toast.makeText(this, "Debes conceder permisos para verificar la conexión a internet", Toast.LENGTH_LONG).show();
            }
        }
    }
    public void downloadImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            Toast.makeText(this, "No hay imagen para descargar", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(imageUrl));
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setTitle("Descargando imagen...");
            request.setDescription("Guardando imagen en almacenamiento...");
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "imagen_descargada.jpg");

            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            if (manager != null) {
                manager.enqueue(request);
                Toast.makeText(this, "Descarga iniciada...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al iniciar la descarga", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error en la descarga: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}