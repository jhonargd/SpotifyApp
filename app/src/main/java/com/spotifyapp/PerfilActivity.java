package com.spotifyapp;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.model.User;

public class PerfilActivity extends AppCompatActivity
{
    private EditText nombresEditText, telefonoEditText, emailEditText;
    ImageView header_image;
    User user;
    String urlImage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        initActionBar();
    }

    private void initActionBar()
    {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Perfil");

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        initUI();
    }

    private void initUI()
    {

        user = (User) getIntent().getParcelableExtra("datosUsuario");
        urlImage = getIntent().getStringExtra("imagen");

        nombresEditText = findViewById(R.id.nombresEditText);
        telefonoEditText = findViewById(R.id.paisEditText);
        emailEditText = findViewById(R.id.emailEditText);

        header_image = (ImageView) findViewById(R.id.header_image);
        Glide.with(PerfilActivity.this)
                .load(urlImage)
                .placeholder(R.mipmap.ic_launcher_round)
                .dontAnimate()
                .override(150, 150)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(header_image);

        header_image.setScaleType(ImageView.ScaleType.CENTER_CROP);

        nombresEditText.setText(user.display_name);
        telefonoEditText.setText(user.country);
        emailEditText.setText(user.email);

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

}
