package com.nassimlnd.flixhub.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.nassimlnd.flixhub.Home.Fragments.CategoryFragment;
import com.nassimlnd.flixhub.LoginActivity;
import com.nassimlnd.flixhub.Model.Media;
import com.nassimlnd.flixhub.Model.Movie;
import com.nassimlnd.flixhub.Network.APIClient;
import com.nassimlnd.flixhub.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeActivity extends AppCompatActivity {

    ImageView imageView;
    LinearLayout categoryContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        imageView = findViewById(R.id.imageView);

        Glide.with(imageView.getContext()).load("https://image.tmdb.org/t/p/w600_and_h900_bestv2/sHIz6PwGkyWD8dewnFnGPQgkWq5.jpg").into(imageView);
        ExecutorService executor =
                Executors.newSingleThreadExecutor();
        Handler handler = new
                Handler(Looper.getMainLooper());

        executor.execute(() -> {
            getMoviesByCategory("FILMS RÉCEMMENT AJOUTÉS", getApplicationContext());
            getMoviesByCategory("MANGAS", getApplicationContext());
            handler.post(() -> {

            });
        });


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d("KEY PRESSED", "BACK");
            }
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

    public void getMoviesByCategory(String category, Context ctx) {
        try {
            String categoryFormatted = URLEncoder.encode(category, "UTF-8");
            String param = "/movies/groups/" + categoryFormatted + "/10";

            ExecutorService executor =
                    Executors.newSingleThreadExecutor();
            Handler handler = new
                    Handler(Looper.getMainLooper());
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    String result = APIClient.callGetMethodWithCookies(param, ctx);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            insertCategoryMedia(category, result);
                        }
                    });
                }
            });
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertCategoryMedia(String categoryName, String result) {
        ArrayList<Media> moviesList = new ArrayList<>();

        try {
            JSONObject jsonResult = new JSONObject(result);
            JSONArray movies = jsonResult.getJSONArray("movies");

            for (int i = 0; i < movies.length(); i++) {
                JSONObject jsonMovie = movies.getJSONObject(i);
                Media movie = new Media();
                Log.d("DEBUG", "insertCategoryMedia: " + jsonMovie.toString());
                movie.setId(Integer.parseInt(jsonMovie.getString("id")));
                movie.setTitle(jsonMovie.getString("title"));
                //movie.setTvg_id(jsonMovie.getString("tvg_id"));
                movie.setTvg_name(jsonMovie.getString("tvgName"));
                movie.setTvg_logo(jsonMovie.getString("tvgLogo"));
                movie.setGroup_title(jsonMovie.getString("groupTitle"));
                movie.setUrl(jsonMovie.getString("url"));
                //movie.setCreatedAt(Date.valueOf(jsonMovie.getString("createdAt")));
                //movie.setUpdatedAt(Date.valueOf(jsonMovie.getString("updatedAt")));

                moviesList.add(movie);
            }

            CategoryFragment categoryFragment = new CategoryFragment(categoryName, moviesList);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.categoryContainer, categoryFragment)
                    .commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void logout(Context ctx) {
        String param = "/auth/logout";
        HashMap<String, String> data = new HashMap<>();

        ExecutorService executor =
                Executors.newSingleThreadExecutor();
        Handler handler = new
                Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String result = APIClient.postMethodWithCookies(param, data, ctx);
                handler.post(() -> {
                    SharedPreferences sharedPreferences = ctx.getSharedPreferences("user", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    ctx.startActivity(new Intent(ctx, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                });
            }
        });
    }
}
