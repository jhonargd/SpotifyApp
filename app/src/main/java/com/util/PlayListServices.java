package com.util;

import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.model.PlayList;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PlayListServices {

    private static final String ENDPOINT = "https://api.spotify.com/v1/me/playlists";
    private SharedPreferences msharedPreferences;
    private RequestQueue mqueue;

    private PlayList playList;

    public PlayListServices(RequestQueue queue, SharedPreferences sharedPreferences) {
        mqueue = queue;
        msharedPreferences = sharedPreferences;
    }

    public PlayList getPlaylist() {
        return playList;
    }

    public void get(final VolleyCallBack callBack) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(ENDPOINT, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                playList = gson.fromJson(response.toString(), PlayList.class);
                callBack.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PlayListServices.this.get(new VolleyCallBack() {
                    @Override
                    public void onSuccess() {

                    }
                });
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = msharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }


        };
        mqueue.add(jsonObjectRequest);
    }


}
