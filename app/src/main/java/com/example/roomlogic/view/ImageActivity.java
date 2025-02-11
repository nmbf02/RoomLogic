//package com.example.roomlogic.view;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.lifecycle.ViewModelProvider;
//import com.bumptech.glide.Glide;
//import com.example.roomlogic.R;
//import com.example.roomlogic.viewmodel.ImageViewModel;
//
//public class ImageActivity extends AppCompatActivity {
//    private ImageViewModel imageViewModel;
//    private EditText etSearch;
//    private Button btnSearch, btnUploadImages;
//    private ImageView imageView;
//    private ActivityResultLauncher<Intent> imagePickerLauncher;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_images);
//
//        etSearch = findViewById(R.id.etSearch);
//        btnSearch = findViewById(R.id.btnSearch);
//        btnUploadImages = findViewById(R.id.btnUploadImages);
//        imageView = findViewById(R.id.imageView);
//
//        imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);
//
//        // 🔹 Buscar imágenes en Unsplash
//        btnSearch.setOnClickListener(v -> {
//            String query = etSearch.getText().toString().trim();
//            if (!query.isEmpty()) {
//                imageViewModel.searchImages(query);
//            }
//        });
//
//        imageViewModel.getImagesLiveData().observe(this, images -> {
//            if (images != null && !images.isEmpty()) {
//                Glide.with(this).load(images.get(0).getUrls().getRegular()).into(imageView);
//            }
//        });
//
//        // 🔹 Subir imágenes a ImgBB
//        btnUploadImages.setOnClickListener(v -> openFilePicker());
//
//        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
//                        Uri imageUri = result.getData().getData();
//                        if (imageUri != null) {
//                            imageViewModel.uploadImage(imageUri);
//                        }
//                    }
//                });
//
//        // 🔹 Observar imágenes subidas y mostrarlas
//        imageViewModel.getUploadedImageUrl().observe(this, url -> {
//            if (url != null) {
//                Glide.with(this).load(url).into(imageView);
//            }
//        });
//    }
//
//    private void openFilePicker() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        imagePickerLauncher.launch(intent);
//    }
//}
//TODO: Borrar clase

