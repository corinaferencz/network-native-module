package com.networkbridge;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableNativeArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NetworkModule extends ReactContextBaseJavaModule {
    NetworkModule(ReactApplicationContext context) {
        super(context);
    }

    @NonNull
    @Override
    public String getName() {
        return "NetworkModule";
    }

    @ReactMethod
    public void getBooks(Promise promise) throws IOException {
        try {
            JSONObject jsonObject = getJSONObjectFromURL("https://www.googleapis.com/books/v1/volumes?q=harry+potter");
            JSONArray items = jsonObject.getJSONArray("items");
            WritableArray titles = new WritableNativeArray();
            for (int i=0, size = items.length(); i < size; i++) {
                JSONObject objects = items.getJSONObject(i);
                JSONObject volumeInfo = objects.getJSONObject("volumeInfo");
                String title = volumeInfo.getString("title");
                titles.pushString(title);
            }
            try {
                if (titles.size() != 0) {
                    promise.resolve(titles);
                }
            } catch (Exception e) {
                promise.reject(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );
        urlConnection.setDoOutput(true);
        urlConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();

        String jsonString = sb.toString();
        System.out.println("JSON: " + jsonString);

        return new JSONObject(jsonString);
    }

}