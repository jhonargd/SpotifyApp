package com.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.model.JsonPlayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreatePlaylistServices {

    private static final String ENDPOINT = "https://api.spotify.com/v1/playlists";
    private SharedPreferences msharedPreferences;
    private RequestQueue mqueue;
    Context context;

    public CreatePlaylistServices(RequestQueue queue, SharedPreferences sharedPreferences, Context context) {
        mqueue = queue;
        msharedPreferences = sharedPreferences;
        this.context = context;
    }

    public void addSongToLibrary(JsonPlayList jsonPlayList) {
        JSONObject payload = preparePutPayload(jsonPlayList);
        JsonObjectRequest jsonObjectRequest = prepareSongLibraryRequest(payload);
        mqueue.add(jsonObjectRequest);
    }

    private JsonObjectRequest prepareSongLibraryRequest(JSONObject payload) {
        return new JsonObjectRequest(Request.Method.POST, ENDPOINT , payload, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(context, "playList Creado",
                        Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "playList NO Creado",
                        Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = msharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
    }

    private JSONObject preparePutPayload(JsonPlayList jsonPlayList) {

        String json = new Gson().toJson(jsonPlayList);

        JSONObject ids = null;
        try {
            ids = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ids;
    }



}
