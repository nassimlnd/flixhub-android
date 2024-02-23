package com.nassimlnd.flixhub.Controller.Media;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.nassimlnd.flixhub.Controller.Network.APIClient;
import com.nassimlnd.flixhub.Model.Media;
import com.nassimlnd.flixhub.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoryListActivity extends AppCompatActivity {

    Toolbar toolbar;
    FlexboxLayout flex1;
    ImageView imageView;
    private ArrayList<Media> data;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_category_list);

        toolbar = findViewById(R.id.toolbar);

        Intent intent = getIntent();
        String category = intent.getStringExtra("category");

        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setTitle(category);
        toolbar.setTitleTextColor(Color.WHITE);


        toolbar.setNavigationOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        flex1 = (FlexboxLayout) findViewById(R.id.flex1);

        try {
            getMoviesbyCategory(category, getApplicationContext());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void getMoviesbyCategory(String category, Context ctx) throws UnsupportedEncodingException {
        String categoryFormatted = URLEncoder.encode(category, "UTF-8");
        String param = "/movies/groups/" + categoryFormatted + "/20";
        ExecutorService executor =
                Executors.newSingleThreadExecutor();
        Handler handler = new
                Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
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

                ImageView image1 = new ImageView(this);
                image1.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.media_card));
                image1.setClipToOutline(true);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(450, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(24,0,0,24);

                image1.setLayoutParams(layoutParams);

                Glide.with(image1.getContext())
                        .load(movie.getTvg_logo())
                        .into(image1);

                flex1.addView(image1);

                image1.setOnClickListener(v -> {
                    Intent intent = new Intent(getApplicationContext(), MediaActivity.class);
                    intent.putExtra("mediaId", movie.getId());

                    startActivity(intent);
                });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
