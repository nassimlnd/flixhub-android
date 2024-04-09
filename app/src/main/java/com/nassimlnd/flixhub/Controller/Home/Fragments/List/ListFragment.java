package com.nassimlnd.flixhub.Controller.Home.Fragments.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.nassimlnd.flixhub.Controller.Media.MovieDetailsActivity;
import com.nassimlnd.flixhub.Model.Interaction;
import com.nassimlnd.flixhub.Model.List;
import com.nassimlnd.flixhub.R;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    LinearLayout emptyListLayout;
    ArrayList<List> lists;
    FlexboxLayout flex1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        emptyListLayout = view.findViewById(R.id.emptyListLayout);
        lists = List.getListByProfile(view.getContext());
        flex1 = view.findViewById(R.id.flex1);
        if (lists.size() > 0) {
            emptyListLayout.setVisibility(View.GONE);

            String orientation = getResources().getConfiguration().orientation == 1 ? "portrait" : "landscape";
            int screenWidth = getResources().getDisplayMetrics().widthPixels - 48;
            int width = orientation.equals("portrait") ? (screenWidth / 2) - 72 : (screenWidth / 4) - 72;

            for (List list : lists) {
                ImageView image1 = new ImageView(view.getContext());

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, 0, 24);

                image1.setLayoutParams(layoutParams);
                image1.setBackground(AppCompatResources.getDrawable(view.getContext(), R.drawable.media_card));
                image1.setClipToOutline(true);

                Glide.with(image1.getContext()).load(list.getMovie().getPoster()).into(image1);

                flex1.addView(image1);
                image1.setOnClickListener(v -> {
                    Intent intent = new Intent(view.getContext(), MovieDetailsActivity.class);
                    intent.putExtra("movieId", list.getMovie().getId());

                    Interaction interaction = new Interaction();
                    interaction.setMediaId(list.getMovie().getId());
                    interaction.setMediaType("movie");
                    interaction.setInteractionType("click");
                    interaction.sendInteraction(view.getContext());

                    startActivity(intent);
                });
            }
        }


        return view;
    }
}
