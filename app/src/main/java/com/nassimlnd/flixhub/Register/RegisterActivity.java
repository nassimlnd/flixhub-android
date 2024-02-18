package com.nassimlnd.flixhub.Register;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    EditText registerEmail;
    EditText registerPassword;
    CheckBox registerTerms;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerLogin = findViewById(R.id.registerLogin);
        toolbar = findViewById(R.id.toolbar);
        registerButton = findViewById(R.id.registerButton);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        registerTerms = findViewById(R.id.registerTerms);

        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        registerLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });

        registerButton.setOnClickListener(v -> {
            String email = registerEmail.getText().toString();
            String password = registerPassword.getText().toString();
            boolean terms = registerTerms.isChecked();

            if (email.isEmpty() || password.isEmpty() || !terms) {
                Toast.makeText(this, R.string.register_input_error, Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                editor.putString("email", email);
                editor.putString("password", password);
                editor.apply();

                startActivity(new Intent(this, RegisterInterestsActivity.class));
            }
        });
    }
}
