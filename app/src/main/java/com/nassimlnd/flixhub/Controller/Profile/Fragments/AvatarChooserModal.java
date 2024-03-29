package com.nassimlnd.flixhub.Controller.Profile.Fragments;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.nassimlnd.flixhub.Model.Profile;
import com.nassimlnd.flixhub.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AvatarChooserModal {

    private BottomSheetDialog modal;
    private LinearLayout modalContent;

    private HashMap<String, ArrayList<String>> avatars = new HashMap<>();

    public AvatarChooserModal(Context ctx, ImageView imageView, Profile profile) {
        modal = new BottomSheetDialog(ctx);

        ArrayList<String> onePieceAvatars = new ArrayList<>();
        for (int i = 1; i <= 13; i++) {
            onePieceAvatars.add("avatar" + i + ".png");
        }

        ArrayList<String> arcaneAvatars = new ArrayList<>();
        for (int i = 14; i <= 25; i++) {
            arcaneAvatars.add("avatar" + i + ".png");
        }

        avatars.put("One Piece", onePieceAvatars);
        avatars.put("Arcane", arcaneAvatars);

        modal.setContentView(R.layout.modal_avatar_picker);

        modalContent = modal.findViewById(R.id.modalAvatarPicker);

        // Initialize bottom sheet view elements
        for (String key : avatars.keySet()) {
            LinearLayout avatarGroup = new LinearLayout(ctx);
            avatarGroup.setOrientation(LinearLayout.VERTICAL);

            LinearLayout.LayoutParams avatarGroupLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            avatarGroupLayoutParams.setMargins(0, 0, 0, 48);

            avatarGroup.setLayoutParams(avatarGroupLayoutParams);

            TextView groupTitle = new TextView(ctx);
            groupTitle.setTextSize(18);
            groupTitle.setText(key);
            groupTitle.setPadding(0, 0, 0, 24);

            avatarGroup.addView(groupTitle);

            HorizontalScrollView avatarScrollView = new HorizontalScrollView(ctx);
            LinearLayout avatarList = new LinearLayout(ctx);
            avatarList.setOrientation(LinearLayout.HORIZONTAL);

            avatarScrollView.addView(avatarList);
            avatarGroup.addView(avatarScrollView);

            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 75, Resources.getSystem().getDisplayMetrics());

            for (String avatar : avatars.get(key)) {
                ImageView avatarImageView = new ImageView(ctx);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, 24, 24);

                avatarImageView.setLayoutParams(layoutParams);
                avatarImageView.setBackground(AppCompatResources.getDrawable(ctx, R.drawable.media_card));
                avatarImageView.setClipToOutline(true);

                // Load the avatar image
                Glide.with(ctx)
                        .load("https://api.nassimlounadi.fr/avatars/" + avatar)
                        .transition(withCrossFade())
                        .into(avatarImageView);

                avatarImageView.setOnClickListener(v -> {
                    Log.d("AvatarChooserModal", "Avatar clicked: " + avatar);

                    // Save the selected avatar
                    Glide.with(imageView.getContext())
                            .load("https://api.nassimlounadi.fr/avatars/" + avatar)
                            .transition(withCrossFade())
                            .into(imageView);

                    dismiss();

                    profile.setAvatar(avatar);
                });

                avatarList.addView(avatarImageView);
            }

            modalContent.addView(avatarGroup);
        }
    }

    public void show() {
        modal.show();
    }

    public void dismiss() {
        modal.dismiss();
    }

}
