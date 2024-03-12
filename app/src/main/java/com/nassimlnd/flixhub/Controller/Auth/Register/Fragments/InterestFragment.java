package com.nassimlnd.flixhub.Controller.Auth.Register.Fragments;

import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.nassimlnd.flixhub.Model.MovieCategory;
import com.nassimlnd.flixhub.Model.SerieCategory;
import com.nassimlnd.flixhub.R;

public class InterestFragment extends Fragment {

    TextView interestFragmentTitle;
    private MovieCategory movieCategory;
    private SerieCategory serieCategory;
    private boolean isSelected = false;
    private String categoryType;

    public InterestFragment(MovieCategory movieCategory) {
        super(R.layout.fragment_register_interest);
        this.movieCategory = movieCategory;
        this.categoryType = "movie";
    }

    public InterestFragment(SerieCategory serieCategory) {
        super(R.layout.fragment_register_interest);
        this.serieCategory = serieCategory;
        this.categoryType = "serie";
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_interest, container, false);

        interestFragmentTitle = view.findViewById(R.id.interest_fragment_title);

        if (movieCategory != null) {
            interestFragmentTitle.setText(movieCategory.getName());

            interestFragmentTitle.setOnClickListener(v -> {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.interest_fragment);
                if (isSelected) {
                    isSelected = false;
                    drawable.setColorFilter(new BlendModeColorFilter(getContext().getColor(R.color.background_light), BlendMode.SRC_ATOP));
                    interestFragmentTitle.setTextColor(getContext().getColor(R.color.primary));
                    interestFragmentTitle.setBackground(drawable);
                } else {
                    isSelected = true;
                    drawable.setColorFilter(new BlendModeColorFilter(getContext().getColor(R.color.primary), BlendMode.SRC_ATOP));
                    interestFragmentTitle.setTextColor(getContext().getColor(R.color.white));
                    interestFragmentTitle.setBackground(drawable);
                }
            });
        } else if (serieCategory != null) {

        }
        return view;
    }

    // Getters and setters

    public MovieCategory getMovieCategory() {
        return movieCategory;
    }

    public void setMovieCategory(MovieCategory movieCategory) {
        this.movieCategory = movieCategory;
    }

    public SerieCategory getSerieCategory() {
        return serieCategory;
    }

    public void setSerieCategory(SerieCategory serieCategory) {
        this.serieCategory = serieCategory;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }
}
