package com.spotifyapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.adapter.PlayListAdapter;
import com.adapter.SongAdapter;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.model.Cancion;
import com.model.Items;
import com.model.User;
import com.util.CancionesServices;
import com.util.VolleyCallBack;

import java.util.ArrayList;

public class DetallePlayList extends AppCompatActivity implements SongAdapter.TotalCallback{

    User user;
    Items items;

    RecyclerView recyclerView;

    private SharedPreferences msharedPreferences;
    private RequestQueue queue;

    ArrayList<Cancion> canciones = new ArrayList<>();
    private SongAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_play_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        cargarDatos();
        cargarCanciones();

    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarCanciones();
    }

    private void cargarDatos() {

        user = (User) getIntent().getParcelableExtra("datosUsuario");
        items = (Items) getIntent().getParcelableExtra("item");
    }

    private void cargarCanciones() {

        msharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(this);

        final CancionesServices cancionesServices = new CancionesServices(queue, msharedPreferences,items);
        cancionesServices.getRecentlyPlayedTracks(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                canciones = cancionesServices.getCanciones();
                renderisarVista();
            }
        });
        

    }

    private void renderisarVista() {

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new SongAdapter(DetallePlayList.this, canciones);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void reproducirSong(int i) {
        String uri = canciones.get(i).getUri();
        Intent launcher = new Intent( Intent.ACTION_VIEW, Uri.parse(uri) );
        startActivity(launcher);

        
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

    public void onClickAgregarCancion(View view) {


        Intent intent = new Intent(DetallePlayList.this, BuscarActivity.class);
        intent.putExtra("datosUsuario", user);
        intent.putExtra("item", items);
        startActivity(intent);

    }
}
