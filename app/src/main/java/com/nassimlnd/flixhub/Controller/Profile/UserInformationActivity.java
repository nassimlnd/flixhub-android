package com.nassimlnd.flixhub.Controller.Profile;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Shader;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.TaskStackBuilder;

import com.nassimlnd.flixhub.Model.User;
import com.nassimlnd.flixhub.R;

public class UserInformationActivity extends AppCompatActivity {

    // View elements
    Toolbar toolbar;
    EditText userFullName, userEmail;
    Button editButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        toolbar = findViewById(R.id.toolbar);
        userFullName = findViewById(R.id.userFullName);
        userEmail = findViewById(R.id.userEmail);
        editButton = findViewById(R.id.editButton);

        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setTitle(R.string.user_informations_title);
        toolbar.setTitleTextColor(Color.WHITE);

        User user = User.getCurrentUser(getApplicationContext());

        userFullName.setText(user.getFullName());
        userEmail.setText(user.getEmail());

        editButton.setOnClickListener(v -> {
            user.setFullName(userFullName.getText().toString());
            user.setEmail(userEmail.getText().toString());

            boolean updated = user.update(getApplicationContext());

            if (updated) {
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("fullName", user.getFullName());
                editor.putString("email", user.getEmail());

                editor.apply();

                Toast.makeText(getApplicationContext(), "User information updated", Toast.LENGTH_SHORT).show();
            }

            finish();
        });
    }
}
