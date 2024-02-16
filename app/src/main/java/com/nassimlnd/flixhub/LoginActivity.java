package com.nassimlnd.flixhub;

import android.content.Intent;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class LoginActivity extends AppCompatActivity {

    Toolbar toolbar;

    TextView loginRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = findViewById(R.id.toolbar);
        loginRegister = findViewById(R.id.loginRegister);

        final Drawable leftArrow = ContextCompat.getDrawable(this, R.drawable.baseline_arrow_back_24);
        leftArrow.setColorFilter(new BlendModeColorFilter(Color.WHITE, BlendMode.SRC_ATOP));

        toolbar.setNavigationIcon(leftArrow);
        toolbar.setNavigationOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        loginRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }
}
