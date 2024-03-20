package com.nassimlnd.flixhub.Controller.Home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.nassimlnd.flixhub.Controller.Home.Fragments.List.ListFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.nassimlnd.flixhub.Controller.Home.Fragments.Discover.DiscoverFragment;
import com.nassimlnd.flixhub.Controller.Home.Fragments.Home.HomeFragment;
import com.nassimlnd.flixhub.Controller.Home.Fragments.Profile.ProfileFragment;
import com.nassimlnd.flixhub.Controller.Auth.LoginActivity;
import com.nassimlnd.flixhub.Controller.Network.APIClient;
import com.nassimlnd.flixhub.R;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottom_nav);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d("KEY PRESSED", "BACK");
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    HomeFragment fragment = new HomeFragment();
                    fragmentManager.beginTransaction().replace(R.id.llContainer, fragment).commit();
                } else if (item.getItemId() == R.id.discover) {
                    DiscoverFragment fragment = new DiscoverFragment();
                    fragmentManager.beginTransaction().replace(R.id.llContainer, fragment).commit();
                } else if (item.getItemId() == R.id.list) {
                    ListFragment fragment = new ListFragment();
                    fragmentManager.beginTransaction().replace(R.id.llContainer, fragment).commit();
                } else if (item.getItemId() == R.id.profile) {
                    ProfileFragment fragment = new ProfileFragment();
                    fragmentManager.beginTransaction().replace(R.id.llContainer, fragment).commit();
                } else {
                    Fragment fragment = null;
                    fragmentManager.beginTransaction().replace(R.id.llContainer, fragment).commit();
                }
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.home);





    }

    public void logout(Context ctx) {
        String param = "/auth/logout";
        HashMap<String, String> data = new HashMap<>();

        ExecutorService executor =
                Executors.newSingleThreadExecutor();
        Handler handler = new
                Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String result = APIClient.postMethodWithCookies(param, data, ctx);
                handler.post(() -> {
                    SharedPreferences sharedPreferences = ctx.getSharedPreferences("user", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    ctx.startActivity(new Intent(ctx, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                });
            }
        });
    }
}
