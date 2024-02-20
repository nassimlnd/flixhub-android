package com.nassimlnd.flixhub.Media;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.nassimlnd.flixhub.Model.Media;
import com.nassimlnd.flixhub.R;

import java.util.ArrayList;

public class CategoryListActivity extends AppCompatActivity {

    Toolbar toolbar;

    private ArrayList<Media> data;

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

    }
}
