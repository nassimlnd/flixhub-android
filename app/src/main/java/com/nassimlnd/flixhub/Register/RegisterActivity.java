package com.nassimlnd.flixhub.Register;

import android.content.Intent;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.nassimlnd.flixhub.LoginActivity;
import com.nassimlnd.flixhub.R;

public class RegisterActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView registerLogin;
    Button registerButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerLogin = findViewById(R.id.registerLogin);
        toolbar = findViewById(R.id.toolbar);
        registerButton = findViewById(R.id.registerButton);

        final Drawable leftArrow = ContextCompat.getDrawable(this, R.drawable.baseline_arrow_back_24);
        leftArrow.setColorFilter(new BlendModeColorFilter(Color.WHITE, BlendMode.SRC_ATOP));

        toolbar.setNavigationIcon(leftArrow);
        toolbar.setNavigationOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        registerLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });

        registerButton.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterInterestsActivity.class));
        });
    }
}
