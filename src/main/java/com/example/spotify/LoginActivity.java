package com.example.spotify;
import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.editTextUsername);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim();

            if (!username.isEmpty()) {
                reproducirSonido(LoginActivity.this, R.raw.write);
                // Crear Intent con el nombre de usuario
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("USERNAME_KEY", username);
                startActivity(intent);

                // Finaliza LoginActivity para evitar volver atrás
                finish();
            } else {
                Toast.makeText(this, "Por favor, ingresa tu nombre de usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void reproducirSonido(Context context, int soundResId) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, soundResId);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
            }
        });
        mediaPlayer.start();
    }
}

