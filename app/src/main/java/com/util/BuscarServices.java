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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BuscarServices {

    private static final String ENDPOINT = "https://api.spotify.com/v1/search";
    private SharedPreferences msharedPreferences;
    private RequestQueue mqueue;
    private String busqueda;

    private ArrayList<Cancion> canciones = new ArrayList<>();

    public BuscarServices(RequestQueue queue, SharedPreferences sharedPreferences, String busqueda) {
        mqueue = queue;
        msharedPreferences = sharedPreferences;
        this.busqueda = busqueda;
    }

    public ArrayList<Cancion> getCanciones() {
        return canciones;
    }

    public ArrayList<Cancion> getRecentlyPlayedTracks(final VolleyCallBack callBack) {

        String endpoint = ENDPOINT + "?q=" + busqueda + "&type=track&market=ES";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();

                        JSONObject object1 = response.optJSONObject("tracks");
                        //object1 = object1.optJSONObject("album");

                        JSONArray jsonArray = object1.optJSONArray("items");
                        for (int n = 0; n < jsonArray.length(); n++) {
                            try {
                                JSONObject object = jsonArray.getJSONObject(n);
                                //object = object.optJSONObject("album");
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
