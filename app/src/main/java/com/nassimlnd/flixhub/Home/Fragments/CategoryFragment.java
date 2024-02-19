package com.nassimlnd.flixhub.Home.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.nassimlnd.flixhub.Model.Media;
import com.nassimlnd.flixhub.Model.Movie;
import com.nassimlnd.flixhub.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CategoryFragment extends Fragment {

    TextView categoryTitle;
    private String title;
    private ArrayList<Media> data;

    public CategoryFragment(String title, ArrayList<Media> data) {
        super(R.layout.fragment_home_category);
        this.title = title;
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
        categoryTitle.setText(title);

        FragmentManager fragmentManager = getChildFragmentManager();

        for (Media media : data) {
            MediaFragment movieFragment = new MediaFragment(media);
            fragmentManager.beginTransaction().add(R.id.categoryMediaContainer, movieFragment).commit();
        }

        return view;
    }
}
