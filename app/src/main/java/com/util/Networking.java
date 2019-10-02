package com.util;

import android.content.SharedPreferences;

import com.loopj.android.http.AsyncHttpClient;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Networking {

    private static AsyncHttpClient client = new AsyncHttpClient();
    public static final MediaType JSON
            = MediaType.parse("application/json");

    public static void postJSON(String url, String json, SharedPreferences msharedPreferences, Callback callback)
    {

        String token = msharedPreferences.getString("token", "");
        String auth = "Bearer " + token;

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", auth)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }

}
