package com.nassimlnd.flixhub;

import android.content.Intent;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class LoginActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Drawable leftArrow = ContextCompat.getDrawable(this, R.drawable.baseline_arrow_back_24);
        leftArrow.setColorFilter(new BlendModeColorFilter(Color.WHITE, BlendMode.SRC_ATOP));

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(leftArrow);


        toolbar.setNavigationOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
    }
}
