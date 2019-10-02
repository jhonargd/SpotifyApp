package com.spotifyapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.model.JsonPlayList;
import com.model.User;
import com.util.CreatePlaylistServices;
import com.util.Networking;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CreatePlaylist extends AppCompatActivity {

    User user;
    EditText nombresEditText, DescEditText;
    Spinner spEstado;

    JsonPlayList jsonPlayList;

    private SharedPreferences msharedPreferences;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_playlist);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        getExtras();

        nombresEditText = (EditText) findViewById(R.id.nombresEditText);
        DescEditText = (EditText) findViewById(R.id.DescEditText);
        spEstado = (Spinner) findViewById(R.id.spEstado);


    }

    private void getExtras() {
        user = (User) getIntent().getParcelableExtra("datosUsuario");
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

    public void onClickAgregarPlayList(View view) {

        if(nombresEditText.getText().toString().isEmpty()){

            Toast.makeText(CreatePlaylist.this, "Debe Ingresar un nombre al Playlist",
                    Toast.LENGTH_LONG).show();

        }else{

            jsonPlayList = new JsonPlayList();
            jsonPlayList.setName(nombresEditText.getText().toString());
            jsonPlayList.setDescription(DescEditText.getText().toString());
            if(spEstado.getSelectedItem().toString().equals("Publico")){
                jsonPlayList.setPublicText(true);
            }else {
                jsonPlayList.setPublicText(false);
            }

            msharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
            queue = Volley.newRequestQueue(this);

            //final CreatePlaylistServices playlistService = new CreatePlaylistServices(queue, msharedPreferences,CreatePlaylist.this);

            String json = new Gson().toJson(jsonPlayList);
            final String url = "https://api.spotify.com/v1/playlists";

            Networking.postJSON(url, json, msharedPreferences, new Callback()
            {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e)
                {
                    Toast.makeText(CreatePlaylist.this, "playList NO Creado",
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

                        Toast.makeText(CreatePlaylist.this, "playList NO Creado",
                                Toast.LENGTH_LONG).show();
                        //Util.MakeToast("MENSAJES ACTUALIZADOS", ActivityChat.this);
                       /* ActivityChat.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });*/

                    }
                }
            });

        }

    }
}
