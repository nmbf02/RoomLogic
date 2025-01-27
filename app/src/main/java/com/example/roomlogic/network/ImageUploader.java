package com.example.roomlogic.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageUploader {

    public static String uploadImage(File imageFile, String serverUrl) throws Exception {
        URL url = new URL(serverUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=boundary");

        OutputStream outputStream = connection.getOutputStream();
        FileInputStream fileInputStream = new FileInputStream(imageFile);

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        fileInputStream.close();
        outputStream.flush();
        outputStream.close();

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return "Imagen subida exitosamente";
        } else {
            throw new Exception("Error al subir la imagen: " + responseCode);
        }
    }
}
