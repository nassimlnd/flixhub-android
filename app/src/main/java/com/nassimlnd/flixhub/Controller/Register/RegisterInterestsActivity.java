package com.nassimlnd.flixhub.Controller.Register;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.nassimlnd.flixhub.Controller.Home.HomeActivity;
import com.nassimlnd.flixhub.Controller.Network.APIClient;
import com.nassimlnd.flixhub.Controller.Register.Fragments.InterestFragment;
import com.nassimlnd.flixhub.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterInterestsActivity extends AppCompatActivity {

    Toolbar toolbar;

    Button registerInterestsSkipButton, registerInterestsSubmitButton;
    ArrayList<InterestFragment> interestFragments = new ArrayList<>();
    private ArrayList<String> topics = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_interests);

        toolbar = findViewById(R.id.toolbar);
        registerInterestsSkipButton = findViewById(R.id.registerInterestsSkipButton);
        registerInterestsSubmitButton = findViewById(R.id.registerInterestsSubmitButton);

        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setTitle(R.string.register_interests_title);
        toolbar.setTitleTextColor(Color.WHITE);

        toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        registerInterestsSkipButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
            editor.putBoolean("haveInterests", false);
            editor.apply();
            startActivity(new Intent(this, RegisterProfileActivity.class));
        });

        registerInterestsSubmitButton.setOnClickListener(v -> onSubmit());

        call("/movies/groups/all");

        Log.d("TAG", "onCreate: " + topics.size());
    }

    public void call(String param) {
        ExecutorService executor =
                Executors.newSingleThreadExecutor();
        Handler handler = new
                Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String data = APIClient.getMethod(param);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        display(data);
                    }
                });
            }
        });
    }

    public void display(String toDisplay) {
        try {
            JSONObject myjson = new JSONObject(toDisplay);
            JSONArray jsonArray = myjson.getJSONArray("groups");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                topics.add(jsonObject.getString("group_title"));
                Log.d("TAG", "display: " + jsonObject.getString("group_title"));

                InterestFragment interestFragment = new InterestFragment(jsonObject.getString("group_title"));
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.interest_fragment_container, interestFragment)
                        .commit();
                interestFragments.add(interestFragment);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void onSubmit() {
        ArrayList<String> selectedInterests = new ArrayList<>();
        for (InterestFragment interestFragment : interestFragments) {
            if (interestFragment.isSelected()) {
                selectedInterests.add(interestFragment.text);
            }
        }

        if (selectedInterests.size() < 3) {
            Toast.makeText(this, R.string.register_interests_error, Toast.LENGTH_SHORT).show();
            return;
        }

        String json = new Gson().toJson(selectedInterests);

        SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
        editor.putString("interests", json);
        editor.putBoolean("haveInterests", true);
        editor.apply();

        startActivity(new Intent(this, HomeActivity.class));
    }
}


