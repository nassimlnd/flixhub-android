package com.nassimlnd.flixhub.Controller.Auth.Register;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.nassimlnd.flixhub.Controller.Home.HomeActivity;
import com.nassimlnd.flixhub.Controller.Network.APIClient;
import com.nassimlnd.flixhub.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterProfileActivity extends AppCompatActivity {

    // Constants


    // View elements
    Toolbar toolbar;
    Button registerSubmit;
    EditText registerProfileName, registerProfileBirthdate;
    Context ctx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_profile);

        toolbar = findViewById(R.id.toolbar);
        registerSubmit = findViewById(R.id.registerProfileSubmit);
        registerProfileName = findViewById(R.id.registerProfileName);
        registerProfileBirthdate = findViewById(R.id.registerProfileBirthdate);
        ctx = getApplicationContext();

        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setTitle(R.string.register_profile_title);
        toolbar.setTitleTextColor(Color.WHITE);

        toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());



        registerSubmit.setOnClickListener(v -> {
            String profileName = registerProfileName.getText().toString();

            /*if (fullName.isEmpty() || nickname.isEmpty() || phoneNumber.isEmpty()) {
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.x);
                drawable.setColorFilter(new BlendModeColorFilter(Color.RED, BlendMode.SRC_IN));
                if (fullName.isEmpty()) {
                    registerFullName.setError("Please enter your full name", drawable);
                } else if (nickname.isEmpty()) {
                    registerNickname.setError("Please enter your nickname", drawable);
                } else if (phoneNumber.isEmpty()) {
                    registerPhoneNumber.setError("Please enter your phone number", drawable);
                }
            } else {
                SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                String email = sharedPreferences.getString("email", "");
                String password = sharedPreferences.getString("password", "");
                boolean haveInterests = sharedPreferences.getBoolean("haveInterests", false);
                String interests = sharedPreferences.getString("interests", "");

                HashMap<String, String> data = new HashMap<>();
                data.put("email", email);
                data.put("password", password);
                data.put("fullName", fullName);
                data.put("phoneNumber", phoneNumber);
                data.put("haveInterests", String.valueOf(haveInterests));
                data.put("interests", interests);

                Log.d("TAG", "onCreate: " + data.toString());
                Log.d("TAG", "Calling /auth/register");

                callRegisterMethod(data, getApplicationContext());
            }*/
        });
    }


}
