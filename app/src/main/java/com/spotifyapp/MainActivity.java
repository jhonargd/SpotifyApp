package com.spotifyapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.adapter.PlayListAdapter;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.model.Items;
import com.model.PlayList;
import com.model.User;
import com.util.PlayListServices;
import com.util.VolleyCallBack;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PlayListAdapter.TotalCallback {

    //componentes graficos
    TextView txtname, txtemail;
    private ImageView headerImageView;
    NavigationView navigationView;
    RecyclerView recyclerView;
    private DrawerLayout drawerLayout;

    private SharedPreferences msharedPreferences;
    private RequestQueue queue;

    User user;
    String urlImage;
    private PlayListAdapter adapter;

    PlayList playList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreatePlaylist.class);
                intent.putExtra("datosUsuario", user);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        InitDrawer();
        cargarPlayList();

    }

    private void InitDrawer()
    {
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener()
        {
            @Override
            public void onDrawerSlide(@NonNull View view, float v)
            {

            }

            @Override
            public void onDrawerOpened(@NonNull View view)
            {
                //ConsultarNotificaciones();
                //Picasso.get().load(DataController.getInstance().getDatosUsuario().getUsuario().getImagen()).into(headerImageView);
                //headerImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            @Override
            public void onDrawerClosed(@NonNull View view)
            {

            }

            @Override
            public void onDrawerStateChanged(int i)
            {

            }
        });

        navigationView = findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);

        user = (User) getIntent().getParcelableExtra("datosUsuario");
        urlImage = getIntent().getStringExtra("imagen");

        headerImageView = header.findViewById(R.id.header_image);
        Glide.with(MainActivity.this)
                .load(urlImage)
                .placeholder(R.mipmap.ic_launcher_round)
                .dontAnimate()
                .override(150, 150)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(headerImageView);

        headerImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        txtname = header.findViewById(R.id.txtname);
        txtname.setText(user.display_name);
        txtemail = header.findViewById(R.id.txtemail);
        txtemail.setText(user.email);

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void toggleDrawer()
    {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT))
        {
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
        else
        {
            drawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    private void cargarPlayList() {

        msharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(this);

        final PlayListServices playListServices = new PlayListServices(queue, msharedPreferences);
        playListServices.get(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                playList = playListServices.getPlaylist();
                renderisarVista();
            }
        });



    }

    private void renderisarVista() {

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new PlayListAdapter(MainActivity.this, playList.items);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_perfil) {

            toggleDrawer();
            Intent intent = new Intent(MainActivity.this, PerfilActivity.class);
            intent.putExtra("datosUsuario", user);
            intent.putExtra("imagen", urlImage);
            startActivity(intent);


        } else if (id == R.id.nav_salir) {
            toggleDrawer();
            finish();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void verDetallePlayList(int i) {

        Items item = playList.items[i];
        Intent intent = new Intent(MainActivity.this, DetallePlayList.class);
        intent.putExtra("datosUsuario", user);
        intent.putExtra("item", item);
        startActivity(intent);


    }
}
