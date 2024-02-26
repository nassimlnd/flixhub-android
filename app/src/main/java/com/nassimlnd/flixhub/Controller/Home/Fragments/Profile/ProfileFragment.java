package com.nassimlnd.flixhub.Controller.Home.Fragments.Profile;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.nassimlnd.flixhub.Controller.GettingStartedActivity;
import com.nassimlnd.flixhub.Controller.Network.APIClient;
import com.nassimlnd.flixhub.Controller.Profile.ProfileChooserActivity;
import com.nassimlnd.flixhub.R;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * It's the fragment that shows the different user's edition options
 * like logout, change password, etc.
 */
public class ProfileFragment extends Fragment {

    // Constants
    private static String BASE_URL = "https://api.nassimlounadi.fr/avatars/";

    // View elements
    TextView profileName;
    ImageView profileAvatar;
    FlexboxLayout logout, changeProfile;

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
        profileAvatar = view.findViewById(R.id.imageView);
        logout = view.findViewById(R.id.logout);
        changeProfile = view.findViewById(R.id.profile_change_profile_button);

        // Get the user's name and email from the shared preferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("profile", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        String avatar = sharedPreferences.getString("avatar", "");

        // Set the user's name and avatar
        profileName.setText(name);
        profileAvatar.setClipToOutline(true);

        Glide.with(profileAvatar.getContext())
                .load(BASE_URL + avatar)
                .transition(withCrossFade())
                .into(profileAvatar);

        // Logout bottom sheet dialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.modal_logout_bottom);

        logout.setOnClickListener((v) -> {
            bottomSheetDialog.show();
            bottomSheetDialog.findViewById(R.id.logout_confirm).setOnClickListener((v1) -> {
                bottomSheetDialog.dismiss();
                logout(getContext());
            });
            bottomSheetDialog.findViewById(R.id.logout_cancel).setOnClickListener((v1) -> bottomSheetDialog.dismiss());
        });

        changeProfile.setOnClickListener((v) -> {
            getActivity().finish();
            startActivity(new Intent(getContext(), ProfileChooserActivity.class));
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
        executor.execute(() -> {
            APIClient.postMethodWithCookies(param, data, ctx);
            handler.post(() -> {
                SharedPreferences sharedPreferences = ctx.getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                getActivity().finish();
                ctx.startActivity(new Intent(ctx, GettingStartedActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            });
        });
    }
}
