package com.nassimlnd.flixhub.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.nassimlnd.flixhub.Controller.Network.APIClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class User {

    // Constants
    private static final String LOGIN_ROUTE = "/auth/login";

    // Attributes
    private String fullName;
    private String email;
    private String nickname;
    private String phoneNumber;
    private String createdAt;
    private String updatedAt;

    public User(String fullName, String email, String nickname, String phoneNumber, String createdAt, String updatedAt) {
        this.fullName = fullName;
        this.email = email;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public User() {
    }

    /**
     * Get the user from the shared preferences
     *
     * @param ctx The application context
     * @return User stored in the shared preferences
     */
    public static User getUserFromSP(Context ctx) {
        User user = new User();
        user.setFullName(ctx.getSharedPreferences("user", 0).getString("fullName", ""));
        user.setEmail(ctx.getSharedPreferences("user", 0).getString("email", ""));
        user.setNickname(ctx.getSharedPreferences("user", 0).getString("nickname", ""));
        user.setPhoneNumber(ctx.getSharedPreferences("user", 0).getString("phoneNumber", ""));
        user.setCreatedAt(ctx.getSharedPreferences("user", 0).getString("createdAt", ""));
        user.setUpdatedAt(ctx.getSharedPreferences("user", 0).getString("updatedAt", ""));
        return user;
    }

    public static boolean login(String email, String password, Context ctx) {
        HashMap<String, String> data = new HashMap<>();
        data.put("email", email);
        data.put("password", password);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            String result = APIClient.postMethod(LOGIN_ROUTE, data, ctx);

            SharedPreferences sharedPreferences = ctx.getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject userObject = jsonObject.getJSONObject("user");
                Log.d("TAG", "handleResult: " + userObject);

                editor.putString("email", userObject.getString("email"));
                editor.putString("fullName", userObject.getString("fullName"));
                editor.putString("nickname", userObject.getString("nickname"));
                editor.putString("phoneNumber", userObject.getString("phoneNumber"));
                editor.putString("createdAt", userObject.getString("createdAt"));
                editor.putString("updatedAt", userObject.getString("updatedAt"));
                //editor.putBoolean("haveInterests", jsonArray.getJSONObject(0).getBoolean("haveInterests"));
                editor.putBoolean("isLoggedIn", true);
                editor.apply();

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
        return true;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
