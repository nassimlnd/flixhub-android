package com.nassimlnd.flixhub.Controller.Media;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.nassimlnd.flixhub.Model.Interaction;
import com.nassimlnd.flixhub.Model.Movie;
import com.nassimlnd.flixhub.Model.MovieCategory;
import com.nassimlnd.flixhub.Model.Serie;
import com.nassimlnd.flixhub.Model.SerieCategory;
import com.nassimlnd.flixhub.R;

import java.util.ArrayList;

public class SerieCategoryListActivity extends AppCompatActivity {
    Toolbar toolbar;
    FlexboxLayout flex1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_category_list);

        toolbar = findViewById(R.id.toolbar);

        Intent intent = getIntent();
        int category = intent.getIntExtra("category", 0);
        String name = intent.getStringExtra("name");
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setTitle(name);
        toolbar.setTitleTextColor(Color.WHITE);

        toolbar.setNavigationOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        flex1 = findViewById(R.id.flex1);

        getContent(this, String.valueOf(category));
    }
    public void getContent(Context ctx, String category) {
        SerieCategory serieCat = SerieCategory.getSerieCategoryById(Integer.parseInt(category), ctx);
        ArrayList<Serie> serieList = Serie.getSeriesByCategory(ctx,serieCat.getId(), 30);

        String orientation = getResources().getConfiguration().orientation == 1 ? "portrait" : "landscape";
        int screenWidth = getResources().getDisplayMetrics().widthPixels - 48;
        int width = orientation.equals("portrait") ? (screenWidth / 2) - 72 : (screenWidth / 4) - 72;

        for (Serie serie : serieList) {
            ImageView image1 = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 24);

            image1.setLayoutParams(layoutParams);
            image1.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.media_card));
            image1.setClipToOutline(true);

            Glide.with(image1.getContext())
                    .load(serie.getPoster())
                    .into(image1);

            flex1.addView(image1);

            image1.setOnClickListener(v -> {
                Intent intent = new Intent(getApplicationContext(), SerieDetailsActivity.class);
                intent.putExtra("serieId", serie.getId());

                Interaction interaction = new Interaction();
                interaction.setMediaId(serie.getId());
                interaction.setMediaType("serie");
                interaction.setInteractionType("click");
                interaction.sendInteraction(getApplicationContext());

                startActivity(intent);
            });
        }
    }
}
