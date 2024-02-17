package com.nassimlnd.flixhub.Register;

import android.content.Intent;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.nassimlnd.flixhub.R;
import com.nassimlnd.flixhub.Register.Fragments.InterestFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterInterestsActivity extends AppCompatActivity {

    Toolbar toolbar;

    Button registerInterestsSkipButton;

    private ArrayList<String> topics = new ArrayList<>();
    ArrayList<InterestFragment> interestFragments = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_interests);

        toolbar = findViewById(R.id.toolbar);
        registerInterestsSkipButton = findViewById(R.id.registerInterestsSkipButton);

        final Drawable leftArrow = ContextCompat.getDrawable(this, R.drawable.baseline_arrow_back_24);
        leftArrow.setColorFilter(new BlendModeColorFilter(Color.WHITE, BlendMode.SRC_ATOP));

        toolbar.setNavigationIcon(leftArrow);
        toolbar.setTitle(R.string.register_interests_title);
        toolbar.setTitleTextColor(Color.WHITE);

        toolbar.setNavigationOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        registerInterestsSkipButton.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterProfileActivity.class));
        });

        call("http://10.0.2.2:3333/movies/groups");
        Log.d("TAG", "onCreate: " + topics.size());
    }

    public String getDataFromHTTP(String param) {
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = null;

        try {
            URL url = new URL(param);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bf = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line = bf.readLine()) != null) {
                result.append(line);
            }
            inputStream.close();
            bf.close();
            conn.disconnect();
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result.toString();
    }

    public void call(String param) {
        ExecutorService executor =
                Executors.newSingleThreadExecutor();
        Handler handler = new
                Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String data = getDataFromHTTP(param);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        display(data);
                    }
                });
            }
        });

    }

    public void display(String toDisplay){
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
}


