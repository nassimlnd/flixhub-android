package com.nassimlnd.flixhub.Controller.Profile;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.TaskStackBuilder;

import com.nassimlnd.flixhub.R;

public class UserInformationActivity extends AppCompatActivity {

    // View elements
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setTitle(R.string.user_informations_title);
        toolbar.setTitleTextColor(Color.WHITE);
    }
}
