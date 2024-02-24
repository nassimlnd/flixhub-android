package com.nassimlnd.flixhub.Controller.Home.Fragments.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.nassimlnd.flixhub.Controller.GettingStartedActivity;
import com.nassimlnd.flixhub.Controller.Network.APIClient;
import com.nassimlnd.flixhub.R;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * It's the fragment that shows the different user's edition options
 * like logout, change password, etc.
 */
public class ProfileFragment extends Fragment {

    // View elements
    TextView profileName;
    TextView profileEmail;
    FlexboxLayout logout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Get the view elements
        profileName = view.findViewById(R.id.profile_name);
        profileEmail = view.findViewById(R.id.profile_email);
        logout = view.findViewById(R.id.logout);

        // Get the user's name and email from the shared preferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", 0);
        String name = sharedPreferences.getString("fullName", "");
        String email = sharedPreferences.getString("email", "");

        // Set the user's name and email
        profileName.setText(name);
        profileEmail.setText(email);

        // Logout bottom sheet dialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.modal_logout_bottom);

        logout.setOnClickListener((v) -> {
            bottomSheetDialog.show();
            bottomSheetDialog.findViewById(R.id.logout_confirm).setOnClickListener((v1) -> {
                bottomSheetDialog.dismiss();
                logout(getContext());
            });
            bottomSheetDialog.findViewById(R.id.logout_cancel).setOnClickListener((v1) -> {
                bottomSheetDialog.dismiss();
            });
        });

        return view;
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
                    SharedPreferences sharedPreferences = ctx.getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    getActivity().finish();
                    ctx.startActivity(new Intent(ctx, GettingStartedActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                });
            }
        });
    }
}
