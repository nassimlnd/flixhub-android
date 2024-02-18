package com.nassimlnd.flixhub;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        imageView = findViewById(R.id.imageView);

        ExecutorService executor =
                Executors.newSingleThreadExecutor();
        Handler handler = new
                Handler(Looper.getMainLooper());

        executor.execute(() -> {
            Bitmap bitmap = downloadImage("https://image.tmdb.org/t/p/w600_and_h900_bestv2/sHIz6PwGkyWD8dewnFnGPQgkWq5.jpg");
            handler.post(() -> {
                imageView.setImageBitmap(bitmap);
            });
        });
    }

    public static Bitmap downloadImage(String url) {
        Bitmap bitmap = null;
        try {
            URL urlObject = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
            InputStream inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
