package com.example.roomlogic.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.roomlogic.R;
import com.example.roomlogic.viewmodel.UploadImageViewModel;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private UploadImageViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(UploadImageViewModel.class);

        Button btnUploadImage = findViewById(R.id.btnUploadImage);

        viewModel.getUploadResult().observe(this, result -> {
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
        });

        viewModel.getErrorResult().observe(this, error -> {
            Toast.makeText(MainActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
        });

        btnUploadImage.setOnClickListener(v -> {
            File imageFile = new File("/path/to/image.jpg");
            String serverUrl = "http://127.0.0.1:8080/upload";
            viewModel.uploadImage(imageFile, serverUrl);
        });
    }
}
