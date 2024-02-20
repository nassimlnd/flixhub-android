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
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.nassimlnd.flixhub.Home.Fragments.CategoryFragment;
import com.nassimlnd.flixhub.LoginActivity;
import com.nassimlnd.flixhub.Model.Media;
import com.nassimlnd.flixhub.Model.Movie;
import com.nassimlnd.flixhub.Network.APIClient;
import com.nassimlnd.flixhub.R;

import org.json.JSONArray;
import org.json.JSONException;
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
    TextView highlightGroupTitle;
    TextView highlightTitle;
    ProgressBar progressBar;
    ScrollView content;

    Media highlightMedia;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        imageView = findViewById(R.id.imageView);
        highlightTitle = findViewById(R.id.highlight_title);
        highlightGroupTitle = findViewById(R.id.highlight_group_title);
        content = findViewById(R.id.content);
        progressBar = findViewById(R.id.loading_spinner);

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.setColorSchemeColors(ContextCompat.getColor(getApplicationContext(), R.color.primary));
        circularProgressDrawable.start();

        ExecutorService executor =
                Executors.newSingleThreadExecutor();
        Handler handler = new
                Handler(Looper.getMainLooper());

        executor.execute(() -> {
            highlightMedia = new Media();
            String result = APIClient.callGetMethodWithCookies("/movies/random", getApplicationContext());
            handler.post(() -> {
                try {
                    JSONObject jsonResult = new JSONObject(result);
                    JSONArray movies = jsonResult.getJSONArray("movie");
                    JSONObject jsonMovie = movies.getJSONObject(0);

                    highlightMedia.setId(Integer.parseInt(jsonMovie.getString("id")));
                    highlightMedia.setTitle(jsonMovie.getString("title"));
                    highlightMedia.setTvg_name(jsonMovie.getString("tvgName"));
                    highlightMedia.setTvg_logo(jsonMovie.getString("tvgLogo"));
                    highlightMedia.setGroup_title(jsonMovie.getString("groupTitle"));
                    highlightMedia.setUrl(jsonMovie.getString("url"));

                    highlightTitle.setText(highlightMedia.getTvg_name());
                    highlightGroupTitle.setText(highlightMedia.getGroup_title());
                    getMoviesByCategory("FILMS RÉCEMMENT AJOUTÉS", getApplicationContext());
                    getMoviesByCategory("MANGAS", getApplicationContext());
                    progressBar.setVisibility(ProgressBar.GONE);
                    content.setVisibility(ScrollView.VISIBLE);

                    Glide.with(imageView.getContext())
                            .load(highlightMedia.getTvg_logo())
                            .placeholder(circularProgressDrawable)
                            .into(imageView);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
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

    public void getRandomMedia(Context ctx) {
        try {
            ExecutorService executor =
                    Executors.newSingleThreadExecutor();
            Handler handler = new
                    Handler(Looper.getMainLooper());

            executor.execute(() -> {

            });
        } catch (Exception e) {
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
                movie.setId(Integer.parseInt(jsonMovie.getString("id")));
                movie.setTitle(jsonMovie.getString("title"));
                //movie.setTvg_id(jsonMovie.getString("tvgId"));
                movie.setTvg_name(jsonMovie.getString("tvgName"));
                movie.setTvg_logo(jsonMovie.getString("tvgLogo"));
                movie.setGroup_title(jsonMovie.getString("groupTitle"));
                movie.setUrl(jsonMovie.getString("url"));

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
