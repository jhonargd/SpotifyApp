package com.spotifyapp;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.adapter.GridAdapter;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.model.Cancion;
import com.model.Items;
import com.model.User;
import com.util.BuscarServices;
import com.util.Networking;
import com.util.VolleyCallBack;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BuscarActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, GridAdapter.TotalCallback{

    EditText txtBuscar;

    User user;
    Items items;

    GridView mGridView;
    private GridAdapter mAdapter;

    private SharedPreferences msharedPreferences;
    private RequestQueue queue;

    ArrayList<Cancion> canciones = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        cargarDatos();

        cargarComponentes();

    }

    private void cargarDatos() {

        user = (User) getIntent().getParcelableExtra("datosUsuario");
        items = (Items) getIntent().getParcelableExtra("item");
    }

    private void cargarComponentes() {

        mGridView = (GridView) findViewById(R.id.gridView);
        txtBuscar = (EditText) findViewById(R.id.txtBuscar);

        mGridView.setOnItemClickListener(BuscarActivity.this);
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                // Pause fetcher to ensure smoother scrolling when flinging
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    // Before Honeycomb pause image loading on scroll to help with performance
                    if (!hasHoneycomb()) {
                        Glide.with(BuscarActivity.this).pauseRequests();
                    }
                } else {
                    Glide.with(BuscarActivity.this).resumeRequests();
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });

    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    public void onClickBuscar(View view) {

        msharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(this);

        final BuscarServices cancionesServices = new BuscarServices(queue, msharedPreferences,txtBuscar.getText().toString());
        cancionesServices.getRecentlyPlayedTracks(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                canciones = cancionesServices.getCanciones();
                renderisarVista();
            }
        });



    }

    private void renderisarVista() {

        if (mAdapter == null) {
            mAdapter = new GridAdapter(this, canciones);
            mGridView.setAdapter(mAdapter);
        } else {
            mAdapter.setData(canciones);
            mAdapter.notifyDataSetChanged();
        }

        if (canciones.size() == 0) {
            Toast.makeText(this, "Busqueda sin resultados!.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



    }

    @Override
    public void agregarSong(int pocision) {

        Cancion cancion = canciones.get(pocision);
        //Toast.makeText(this, cancion.getUri(), Toast.LENGTH_SHORT).show();
        String uri = cancion.getUri().replace(":","%3A");

        String json = new Gson().toJson("");
        final String url = "https://api.spotify.com/v1/playlists/"+ items.id +"/tracks?uris=" + uri;

        Networking.postJSON(url, json, msharedPreferences, new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                Toast.makeText(BuscarActivity.this, "No fue posible agregar esta canción al Playlist",
                        Toast.LENGTH_LONG).show();
                //Util.MakeToast("Ocurrió un problema con el servidor", ActivityChat.this);
                //progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {

                String respuesta = response.message();

                if (response.message().equals("OK"))
                {

                    Toast.makeText(BuscarActivity.this, "Canción Agregada",
                            Toast.LENGTH_LONG).show();


                }
            }
        });

    }
}
