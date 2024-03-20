package com.nassimlnd.flixhub.Controller.Media;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.nassimlnd.flixhub.Model.Interaction;
import com.nassimlnd.flixhub.Model.Media;
import com.nassimlnd.flixhub.Model.Movie;
import com.nassimlnd.flixhub.Model.MovieCategory;
import com.nassimlnd.flixhub.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MovieCategoryListActivity extends AppCompatActivity {

    Toolbar toolbar;
    FlexboxLayout flex1;

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

        flex1 = findViewById(R.id.flex1);

        getContent(this, category);
    }

    public void getContent(Context ctx, String category) {
        MovieCategory movieCategory = MovieCategory.getCategoryByName(category, ctx);
        ArrayList<Movie> moviesList = Movie.getMoviesByCategory(String.valueOf(movieCategory.getId()), ctx, 30);

        for (Movie movie : moviesList) {
            ImageView image1 = new ImageView(this);
            image1.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.media_card));
            image1.setClipToOutline(true);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(450, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 24);

            image1.setLayoutParams(layoutParams);

            Glide.with(image1.getContext())
                    .load(movie.getPoster())
                    .into(image1);

            flex1.addView(image1);

            image1.setOnClickListener(v -> {
                Intent intent = new Intent(getApplicationContext(), MovieDetailsActivity.class);
                intent.putExtra("movieId", movie.getId());

                Interaction interaction = new Interaction();
                interaction.setMediaId(movie.getId());
                interaction.setMediaType("movie");
                interaction.setInteractionType("click");
                interaction.sendInteraction(getApplicationContext());

                startActivity(intent);
            });
        }
    }
}
