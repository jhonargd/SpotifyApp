package com.spotifyapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.model.User;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.util.UserService;
import com.util.VolleyCallBack;


import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;

public class SplashActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "16f0b1d26c774e96950d9b29d8136280";
    private static final String REDIRECT_URI = "com.spotifyapp://callback"; //com.spotifyapp
    private static final int REQUEST_CODE = 1337;
    //private static final String SCOPES = "user-read-recently-played,user-library-modify,user-read-email,user-read-private";
    private static final String SCOPES = "user-library-modify, playlist-read-collaborative, user-read-currently-playing, user-read-recently-played, user-top-read, user-read-playback-state, user-modify-playback-state, user-read-private, user-read-email, streaming, user-follow-read, user-follow-modify, user-library-read, playlist-read-private, playlist-modify-public,playlist-modify-private";
    private SharedPreferences.Editor editor;
    private SharedPreferences msharedPreferences;

    private RequestQueue queue;

    //private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        authenticateSpotify();

        msharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(this);

    }

    private void authenticateSpotify() {
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{SCOPES});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, data);

            switch (response.getType()) {

                // Response was successful and contains auth token
                case TOKEN:
                    editor = getSharedPreferences("SPOTIFY", 0).edit();
                    editor.putString("token", response.getAccessToken());
                    //String cadena = response.getAccessToken();
                    editor.apply();
                    obtenerInformacion();
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases

            }
        }

    }

    private void obtenerInformacion() {

        final UserService userService = new UserService(queue, msharedPreferences);
        userService.get(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                User user = userService.getUser();
                editor = SplashActivity.this.getSharedPreferences("SPOTIFY", 0).edit();
                editor.putString("userid", user.id);
                Log.d("STARTING", "GOT USER INFORMATION");
                // We use commit instead of apply because we need the information stored immediately
                editor.commit();

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("datosUsuario", user);
                if(user.images.length > 0){
                    intent.putExtra("imagen", user.images[0].url);
                }
                startActivity(intent);
                finish();

            }
        });

    }

    private void startMainActivity() {
    }
}
