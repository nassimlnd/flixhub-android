package com.nassimlnd.flixhub.Controller.Home.Fragments.Home.Fragments;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.nassimlnd.flixhub.Controller.Media.MediaActivity;
import com.nassimlnd.flixhub.Model.Media;
import com.nassimlnd.flixhub.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MediaFragment extends Fragment {

    RelativeLayout mediaLayout;
    ImageView mediaImage;
    private Media media;

    public MediaFragment(Media media) {
        super(R.layout.fragment_media);
        this.media = media;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media, container, false);

        mediaImage = view.findViewById(R.id.imageView);
        mediaLayout = view.findViewById(R.id.mediaContainer);

        mediaImage.setClipToOutline(true);

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(getContext());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        mediaLayout.setOnClickListener(v -> {
            Intent intent = new Intent(view.getContext(), MediaActivity.class);
            intent.putExtra("mediaId", media.getId());
            startActivity(intent);
        });

        if (media.getTvg_logo().equals("https://image.tmdb.org/t/p/w600_and_h900_bestv2")) {
            media.setTvg_logo("");
        }

        Glide.with(mediaImage.getContext())
                .load(media.getTvg_logo())
                .transition(withCrossFade())
                .error(R.drawable.image)
                .into(mediaImage);


        return view;
    }
}
