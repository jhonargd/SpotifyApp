package com.util;

import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.model.Cancion;
import com.model.Items;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CancionesServices {

    private static final String ENDPOINT = "https://api.spotify.com/v1/playlists/";
    private SharedPreferences msharedPreferences;
    private RequestQueue mqueue;

    Items playlist;
    private ArrayList<Cancion> canciones = new ArrayList<>();

    public CancionesServices(RequestQueue queue, SharedPreferences sharedPreferences, Items playlist) {
        mqueue = queue;
        msharedPreferences = sharedPreferences;
        this.playlist = playlist;
    }

    public ArrayList<Cancion> getCanciones() {
        return canciones;
    }

    public ArrayList<Cancion> getRecentlyPlayedTracks(final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/me/player/recently-played";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        JSONArray jsonArray = response.optJSONArray("items");
                        for (int n = 0; n < jsonArray.length(); n++) {
                            try {
                                JSONObject object = jsonArray.getJSONObject(n);
                                object = object.optJSONObject("track");
                                Cancion song = gson.fromJson(object.toString(), Cancion.class);
                                canciones.add(song);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        callBack.onSuccess();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

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
        return canciones;
    }

}
