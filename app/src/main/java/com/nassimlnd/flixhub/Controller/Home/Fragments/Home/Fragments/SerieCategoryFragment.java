package com.nassimlnd.flixhub.Controller.Home.Fragments.Home.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.nassimlnd.flixhub.Controller.Media.SerieCategoryListActivity;
import com.nassimlnd.flixhub.Model.Serie;
import com.nassimlnd.flixhub.Model.SerieCategory;
import com.nassimlnd.flixhub.R;

import java.util.ArrayList;

public class SerieCategoryFragment extends Fragment {

    // View elements
    TextView seeAll;
    TextView categoryTitle;

    // Data
    private final SerieCategory category;
    private final ArrayList<Serie> data;

    public SerieCategoryFragment(SerieCategory category, ArrayList<Serie> data) {
        super(R.layout.fragment_home_category);
        this.category = category;
        this.data = data;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_category, container, false);

        categoryTitle = view.findViewById(R.id.categoryTitle);
        seeAll = view.findViewById(R.id.seeAll);

        categoryTitle.setText(category.getName());

        seeAll.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SerieCategoryListActivity.class);
            intent.putExtra("category", category.getId());
            intent.putExtra("name", category.getName());
            startActivity(intent);
        });

        FragmentManager fragmentManager = getChildFragmentManager();

        for (Serie serie : data) {
            MediaFragment mediaFragment = new MediaFragment(serie);
            fragmentManager.beginTransaction().add(R.id.categoryMediaContainer, mediaFragment).commit();
        }

        return view;
    }
}
