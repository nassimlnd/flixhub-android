package com.nassimlnd.flixhub.Register;

import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.nassimlnd.flixhub.R;

public class RegisterProfileActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_profile);

        toolbar = findViewById(R.id.toolbar);

        final Drawable leftArrow = ContextCompat.getDrawable(this, R.drawable.baseline_arrow_back_24);
        leftArrow.setColorFilter(new BlendModeColorFilter(Color.WHITE, BlendMode.SRC_ATOP));

        toolbar.setNavigationIcon(leftArrow);
        toolbar.setTitle(R.string.register_profile_title);
        toolbar.setTitleTextColor(Color.WHITE);

        toolbar.setNavigationOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
    }
}
