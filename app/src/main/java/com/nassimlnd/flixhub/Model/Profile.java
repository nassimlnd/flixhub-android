package com.nassimlnd.flixhub.Model;

import android.content.Context;
import android.util.Log;

import com.nassimlnd.flixhub.Controller.Network.APIClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Profile {

    // Constants
    public static final String TAG = "Profile";
    public static final String PROFILE_ROUTE = "/profile";

    // Attributes
    private int id;
    private String name;
    private String avatar;
    private String birthdate;
    private String movieInterests;
    private String serieInterests;

    public Profile(int id, String name, String avatar, String birthdate, String movieInterests, String serieInterests) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.birthdate = birthdate;
        this.movieInterests = movieInterests;
        this.serieInterests = serieInterests;
    }

    public Profile() {

    }

    // Back-end methods

    public static ArrayList<Profile> getProfiles(Context ctx) {
        try {
            ArrayList<Profile> profiles = new ArrayList<>();

            ExecutorService executor = Executors.newSingleThreadExecutor();
            CountDownLatch latch = new CountDownLatch(1);

            executor.execute(() -> {
                // Fetch profiles from the database
                String result = APIClient.callGetMethodWithCookies(PROFILE_ROUTE, ctx);

                Log.d(TAG, "getProfiles: " + result);
                // Parse the result
                try {
                    JSONArray profilesArray = new JSONArray(result);
                    for (int i = 0; i < profilesArray.length(); i++) {
                        profiles.add(new Profile(
                                profilesArray.getJSONObject(i).getInt("id"),
                                profilesArray.getJSONObject(i).getString("name"),
                                profilesArray.getJSONObject(i).getString("avatar"),
                                profilesArray.getJSONObject(i).getString("birthdate"),
                                profilesArray.getJSONObject(i).getString("movieInterests"),
                                profilesArray.getJSONObject(i).getString("serieInterests")
                        ));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                latch.countDown();
            });

            latch.await();
            return profiles;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Profile createProfile(HashMap<String, String> data, Context ctx) {
        Profile profile = new Profile();
        try {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            CountDownLatch latch = new CountDownLatch(1);

            executorService.execute(() -> {
                // Post the new profile
                String result = APIClient.postMethodWithCookies(PROFILE_ROUTE, data, ctx);
                // Parse the result
                try {
                    JSONObject profileObject = new JSONObject(result);

                    profile.setAvatar(profileObject.getString("avatar"));
                    profile.setId(profileObject.getInt("id"));
                    profile.setName(profileObject.getString("name"));
                    profile.setBirthdate(profileObject.getString("birthdate"));
                    profile.setMovieInterests(profileObject.getString("movieInterests"));
                    profile.setSerieInterests(profileObject.getString("serieInterests"));
                    latch.countDown();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });

            latch.await();
            return profile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean deleteProfile(Context ctx, int id) {
        try {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            CountDownLatch latch = new CountDownLatch(1);

            HashMap<String, String> data = new HashMap<>();
            data.put("id", String.valueOf(id));

            executorService.execute(() -> {
                String result = APIClient.postMethodWithCookies(PROFILE_ROUTE + "/delete", data, ctx);

                Log.d(TAG, "deleteProfile: " + result);
                latch.countDown();
            });
            latch.await();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Profile updateProfile(Context ctx, Profile profile) {
        try {
            Profile updatedProfile = new Profile();

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            CountDownLatch latch = new CountDownLatch(1);

            executorService.execute(() -> {
                String result = APIClient.postMethodWithCookies(PROFILE_ROUTE + "/update", profile.toHashMap(), ctx);

                Log.d(TAG, "updateProfile: " + result);

                try {
                    JSONObject profileObject = new JSONObject(result);

                    updatedProfile.setAvatar(profileObject.getString("avatar"));
                    updatedProfile.setId(profileObject.getInt("id"));
                    updatedProfile.setBirthdate(profileObject.getString("birthdate"));
                    updatedProfile.setMovieInterests(profileObject.getString("movieInterests"));
                    updatedProfile.setSerieInterests(profileObject.getString("serieInterests"));
                    updatedProfile.setName(profileObject.getString("name"));

                    if (updatedProfile.equals(profile)) {
                        latch.countDown();
                    } else {
                        throw new RuntimeException("Profile not updated");
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });

            latch.await();
            return updatedProfile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void eraseProfileHistory(Context ctx) {
        try {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            CountDownLatch latch = new CountDownLatch(1);

            HashMap<String, String> data = new HashMap<>();
            data.put("id", String.valueOf(id));

            executorService.execute(() -> {
                String result = APIClient.postMethodWithCookies(PROFILE_ROUTE + "/history/remove", data, ctx);
                Log.d(TAG, "eraseProfileHistory: " + result);
                latch.countDown();
            });

            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Getters and setters

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMovieInterests() {
        return movieInterests;
    }

    public void setMovieInterests(String movieInterests) {
        this.movieInterests = movieInterests;
    }

    public String getSerieInterests() {
        return serieInterests;
    }

    public void setSerieInterests(String serieInterests) {
        this.serieInterests = serieInterests;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public HashMap<String, String> toHashMap() {
        HashMap<String, String> data = new HashMap<>();
        data.put("id", String.valueOf(id));
        data.put("name", name);
        data.put("avatar", avatar);
        data.put("birthdate", birthdate);
        data.put("movieInterests", movieInterests);
        data.put("serieInterests", serieInterests);
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return getId() == profile.getId() && Objects.equals(getName(), profile.getName()) && Objects.equals(getAvatar(), profile.getAvatar()) && Objects.equals(getBirthdate(), profile.getBirthdate()) && Objects.equals(getMovieInterests(), profile.getMovieInterests()) && Objects.equals(getSerieInterests(), profile.getSerieInterests());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getAvatar(), getBirthdate(), getMovieInterests(), getSerieInterests());
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", movieInterests='" + movieInterests + '\'' +
                ", serieInterests='" + serieInterests + '\'' +
                '}';
    }
}
