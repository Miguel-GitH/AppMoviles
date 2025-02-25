package com.example.spotify;

import static android.content.Intent.getIntent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "onCreate: Iniciando aplicación");

        // Cargar el fragmento por defecto (CancionesFragment)
        loadFragment(new CancionesFragment());
    }

    public void cambiarFragment(View v) {
        Fragment fragment = null;

        if (v.getId() == R.id.linear_canciones) {
            Log.d(TAG, "Cambiando a Canciones");
            fragment = new CancionesFragment();
        } else if (v.getId() == R.id.linear_favoritos) {
            Log.d(TAG, "Cambiando a Favoritos");
            fragment = new FavoritosFragment();
        } else if (v.getId() == R.id.linear_perfil) {
            Log.d(TAG, "Cambiando a Perfil");
            fragment = new PerfilFragment();
            // Recoger el nombre de usuario desde el Intent
            String username = getIntent().getStringExtra("USERNAME_KEY");
            if (username == null) {
                username = "Usuario"; // Valor por defecto
            }
            Bundle bundle = new Bundle();
            bundle.putString("USERNAME_KEY", username);
            fragment.setArguments(bundle);
        }

        if (fragment != null) {
            loadFragment(fragment);
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }
    @Override
    protected void onStop() {
        super.onStop();

        Log.i(TAG, "onStop: stop");
    }
}
