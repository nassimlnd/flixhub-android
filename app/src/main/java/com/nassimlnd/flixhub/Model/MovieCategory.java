package com.nassimlnd.flixhub.Model;

import android.content.Context;

import com.nassimlnd.flixhub.Controller.Network.APIClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MovieCategory {

    // Attributes
    private int id;
    private String name;

    // Constructor
    public MovieCategory(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public MovieCategory() {

    }

    // Back-end methods

    public static ArrayList<MovieCategory> getAll(Context ctx) {
        ArrayList<MovieCategory> categories = new ArrayList<>();
        try {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            CountDownLatch countDownLatch = new CountDownLatch(1);

            executorService.execute(() -> {
                String result = APIClient.callGetMethodWithCookies("/movies/categories", ctx);
                try {
                    JSONArray movieCategoriesArray = new JSONArray(result);
                    for (int i = 0; i < movieCategoriesArray.length(); i++) {
                        JSONObject movieCategoryObject = movieCategoriesArray.getJSONObject(i);
                        MovieCategory movieCategory = new MovieCategory();
                        movieCategory.setId(movieCategoryObject.getInt("id"));
                        movieCategory.setName(movieCategoryObject.getString("name"));
                        categories.add(movieCategory);
                    }
                    countDownLatch.countDown();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });
            countDownLatch.await();
            return categories;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static MovieCategory getMovieCategoryById(Context ctx, int movieCategoryId) {
        MovieCategory movieCategory = new MovieCategory();
        try {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            CountDownLatch countDownLatch = new CountDownLatch(1);

            executorService.execute(() -> {
                String result = APIClient.callGetMethodWithCookies("/movies/category/" + movieCategoryId, ctx);
                try {
                    JSONObject movieCategoryObject = new JSONObject(result);
                    movieCategory.setId(movieCategoryObject.getInt("id"));
                    movieCategory.setName(movieCategoryObject.getString("name"));
                    countDownLatch.countDown();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });

            countDownLatch.await();
            return movieCategory;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

//    public static MovieCategory getMovieCategoryByName(Context ctx, String category) {
//        MovieCategory movieCategory = new MovieCategory();
//        try {
//            ExecutorService executorService = Executors.newSingleThreadExecutor();
//            CountDownLatch countDownLatch = new CountDownLatch(1);
//
//            executorService.execute(() -> {
//                String result = APIClient.callGetMethodWithCookies("/movies/category/")
//            });
//        }
//    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
