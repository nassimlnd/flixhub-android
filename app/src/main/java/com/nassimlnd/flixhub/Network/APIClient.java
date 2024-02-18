package com.nassimlnd.flixhub.Network;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class APIClient {

    public static final String BASE_URL = "http://10.0.2.2:3333";

    public static String getMethod(String param) {
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = null;

        try {
            URL url = new URL(BASE_URL + param);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bf = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line = bf.readLine()) != null) {
                result.append(line);
            }
            inputStream.close();
            bf.close();
            conn.disconnect();
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result.toString();
    }

    public static String postMethod(String param, HashMap<String, String> data) {
        HttpURLConnection conn = null;

        try {
            URL url = new URL(BASE_URL + param);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            OutputStream outputStream = conn.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            BufferedWriter writer = new BufferedWriter(outputStreamWriter);
            if (data != null) {
                for (String key : data.keySet()) {
                    writer.write(key + "=" + data.get(key) + "&");
                }
            }

            writer.close();
            outputStream.close();

            int responseCode = conn.getResponseCode();
            Log.d("TAG", "postMethod: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                conn.disconnect();
                return response.toString();
            } else {
                conn.disconnect();
                throw new RuntimeException("HTTP response code: " + responseCode);
            }
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void callPostMethod(String param, HashMap<String, String> data) {
        ExecutorService executor =
                Executors.newSingleThreadExecutor();
        Handler handler = new
                Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String result = postMethod(param, data);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        display(result);
                    }
                });
            }
        });
    }

    public static String callGetMethod(String param) {
        final StringBuilder[] result = {new StringBuilder()};
        ExecutorService executor =
                Executors.newSingleThreadExecutor();
        Handler handler = new
                Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                result[0] = new StringBuilder(getMethod(param));
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        display(result[0].toString());
                    }
                });
            }
        });
        return result[0].toString();
    }

    private static void display(String result) {
    }

    public static void deleteMethod(String param) {
        // ...
    }

}
