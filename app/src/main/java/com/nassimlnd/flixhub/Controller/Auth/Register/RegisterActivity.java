package com.nassimlnd.flixhub.Controller.Auth.Register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.nassimlnd.flixhub.Controller.Auth.LoginActivity;
import com.nassimlnd.flixhub.Controller.MainActivity;
import com.nassimlnd.flixhub.Model.User;
import com.nassimlnd.flixhub.R;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    LinearLayout registerLayout;
    Toolbar toolbar;
    TextView registerLogin;
    Button registerButton;
    EditText registerEmail, registerPassword, registerFullName;
    CheckBox registerTerms;
    ProgressBar loadingSpinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerLogin = findViewById(R.id.registerLogin);
        toolbar = findViewById(R.id.toolbar);
        registerButton = findViewById(R.id.registerButton);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        registerFullName = findViewById(R.id.registerFullName);
        registerTerms = findViewById(R.id.registerTerms);
        registerLayout = findViewById(R.id.registerLayout);
        loadingSpinner = findViewById(R.id.loading_spinner);

        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        registerLogin.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));

        registerButton.setOnClickListener(v -> {
            String email = registerEmail.getText().toString();
            String password = registerPassword.getText().toString();
            String fullName = registerFullName.getText().toString();
            boolean terms = registerTerms.isChecked();

            if (email.isEmpty() || fullName.isEmpty() || password.isEmpty() || !terms) {
                Toast.makeText(this, R.string.register_input_error, Toast.LENGTH_SHORT).show();
            } else {
                registerLayout.setVisibility(LinearLayout.GONE);
                loadingSpinner.setVisibility(ProgressBar.VISIBLE);

                HashMap<String, String> data = new HashMap<>();
                data.put("email", email);
                data.put("password", password);
                data.put("fullName", fullName);

                try {
                    User user = User.register(data, this);
                    if (Objects.equals(user.getEmail(), email)) {
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.putExtra("flash_success", R.string.register_success);
                        startActivity(intent);
                    } else {
                        registerLayout.setVisibility(LinearLayout.VISIBLE);
                        loadingSpinner.setVisibility(ProgressBar.GONE);
                        Toast.makeText(this, R.string.register_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.d("TAG", "onCreate: " + e.getMessage());
                    Toast.makeText(this, R.string.register_error, Toast.LENGTH_SHORT).show();
                }


            }
        });
    }


}
