package com.nassimlnd.flixhub.Register;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.nassimlnd.flixhub.HomeActivity;
import com.nassimlnd.flixhub.Network.APIClient;
import com.nassimlnd.flixhub.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterProfileActivity extends AppCompatActivity {

    Toolbar toolbar;

    Button registerSubmit;
    EditText registerFullName;
    EditText registerNickname;
    EditText registerPhoneNumber;
    static Context ctx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_profile);

        toolbar = findViewById(R.id.toolbar);
        registerSubmit = findViewById(R.id.registerProfileSubmit);
        registerFullName = findViewById(R.id.registerProfileFullName);
        registerNickname = findViewById(R.id.registerProfileNickname);
        registerPhoneNumber = findViewById(R.id.registerProfilePhoneNumber);
        ctx = getApplicationContext();

        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setTitle(R.string.register_profile_title);
        toolbar.setTitleTextColor(Color.WHITE);

        toolbar.setNavigationOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        registerSubmit.setOnClickListener(v -> {
            String fullName = registerFullName.getText().toString();
            String nickname = registerNickname.getText().toString();
            String phoneNumber = registerPhoneNumber.getText().toString();

            if (fullName.isEmpty() || nickname.isEmpty() || phoneNumber.isEmpty()) {
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

                HashMap<String, String> data = new HashMap<>();
                data.put("email", email);
                data.put("password", password);
                data.put("fullName", fullName);
                data.put("nickname", nickname);
                data.put("phoneNumber", phoneNumber);
                data.put("haveInterests", String.valueOf(haveInterests));

                Log.d("TAG", "onCreate: " + data.toString());
                Log.d("TAG", "Calling /auth/register");

                callRegisterMethod(data);
            }
        });
    }

    public static void callRegisterMethod(HashMap<String, String> data) {
        String param = "/auth/register";

        ExecutorService executor =
                Executors.newSingleThreadExecutor();
        Handler handler = new
                Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String result = APIClient.postMethod(param, data);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        handleResult(result);
                    }
                });
            }
        });
    }

    public static void handleResult(String result) {

        SharedPreferences sharedPreferences = ctx.getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject userObject = jsonObject.getJSONObject("user");
            Log.d("TAG", "handleResult: " + userObject);

            editor.putString("email", userObject.getString("email"));
            editor.putString("fullName", userObject.getString("fullName"));
            editor.putString("nickname", userObject.getString("nickname"));
            editor.putString("phoneNumber", userObject.getString("phoneNumber"));
            editor.putString("createdAt", userObject.getString("createdAt"));
            editor.putString("updatedAt", userObject.getString("updatedAt"));
            //editor.putBoolean("haveInterests", jsonArray.getJSONObject(0).getBoolean("haveInterests"));
            editor.putBoolean("isLoggedIn", true);
            editor.apply();

            Log.d("TAG", "handleResult: " + userObject.getString("email"));

            ctx.startActivity(new Intent(ctx, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }
}
