package com.example.spotify;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CancionesFragment extends Fragment {

    private List<Cancion> cancionList;
    private View reproductor;
    private static MediaPlayer mediaPlayer;
    private TextView txtTitulo, txtTiempoRestante;
    private SeekBar seekBar;
    private ImageButton btnPlayPause, btnRew, btnFf, btnStop, btnNext, btnPrev;
    private Handler handler = new Handler();
    private static boolean isPlaying = false;
    private ImageView image;
    private static Cancion cancionActual;
    private static int cancionIndex = -1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_canciones, container, false);

        // Inicializar vistas
        LinearLayout contenedorCanciones = view.findViewById(R.id.contenedor_canciones);
        reproductor = view.findViewById(R.id.reproductor);
        txtTitulo = view.findViewById(R.id.txt_titulo);
        txtTiempoRestante = view.findViewById(R.id.txt_tiempo_restante);
        seekBar = view.findViewById(R.id.seekBar);
        btnPlayPause = view.findViewById(R.id.btn_play_pause);
        btnRew = view.findViewById(R.id.btn_rew);
        btnFf = view.findViewById(R.id.btn_ff);
        btnStop = view.findViewById(R.id.btn_stop);
        btnNext = view.findViewById(R.id.btn_next);
        btnPrev = view.findViewById(R.id.btn_prev);
        image = view.findViewById(R.id.imageView);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        // Asignar listeners a los botones
        btnPlayPause.setOnClickListener(v -> togglePlayPause());
        btnRew.setOnClickListener(v -> rew());
        btnFf.setOnClickListener(v -> ff());
        btnStop.setOnClickListener(v -> stop());
        btnNext.setOnClickListener(v -> siguienteCancion());
        btnPrev.setOnClickListener(v -> anteriorCancion());

        // Cargar la lista de canciones
        cancionList = new ArrayList<>();
        cancionList.add(new Cancion("Oporto", "Pepe", R.drawable.oporto, R.raw.audio1));
        cancionList.add(new Cancion("Comidas", "Juanito", R.drawable.nota_musical, R.raw.audio2));
        cancionList.add(new Cancion("Tomates", "Paquito", R.drawable.tomato, R.raw.audio3));
        cancionList.add(new Cancion("Patata", "Ruben", R.drawable.patata, R.raw.audio4));

        for (Cancion cancion : cancionList) {
            View itemView = inflater.inflate(R.layout.item_cancion, contenedorCanciones, false);
            TextView titulo = itemView.findViewById(R.id.text_titulo);
            TextView artista = itemView.findViewById(R.id.text_artista);
            ImageView imagen = itemView.findViewById(R.id.image_cancion);
            ImageButton favoritoButton = itemView.findViewById(R.id.button_favorito);

            titulo.setText(cancion.getTitulo());
            artista.setText(cancion.getArtista());
            imagen.setImageResource(cancion.getImagenResId());
            favoritoButton.setImageResource(ClassList.listaFavoritos.contains(cancion) ? R.drawable.small_star_icon : R.drawable.estrella_blanca);

            favoritoButton.setOnClickListener(v -> {
                if (ClassList.listaFavoritos.contains(cancion)) {
                    ClassList.listaFavoritos.remove(cancion);
                    favoritoButton.setImageResource(R.drawable.estrella_blanca);
                    Toast.makeText(v.getContext(), "Eliminado de Favoritos", Toast.LENGTH_SHORT).show();
                } else {
                    ClassList.listaFavoritos.add(cancion);
                    favoritoButton.setImageResource(R.drawable.small_star_icon);
                    Toast.makeText(v.getContext(), "Añadido a Favoritos", Toast.LENGTH_SHORT).show();
                }
            });

            itemView.setOnClickListener(v -> reproducirCancion(cancion));
            contenedorCanciones.addView(itemView);
        }

        return view;
    }

    // Reproducir una canción
    public void reproducirCancion(Cancion cancion) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            handler.removeCallbacks(updateSeekBar);
        }

        mediaPlayer = MediaPlayer.create(getContext(), cancion.getAudioResId());
        if (mediaPlayer == null) {
            Toast.makeText(getContext(), "Error al cargar el audio", Toast.LENGTH_SHORT).show();
            return;
        }

        mediaPlayer.start();
        isPlaying = true;
        cancionActual = cancion;
        cancionIndex = cancionList.indexOf(cancion);
        actualizarUIReproductor(cancion);
        mediaPlayer.setOnCompletionListener(mp -> siguienteCancion());
        handler.post(updateSeekBar);
    }

    // Actualizar la interfaz del reproductor
    private void actualizarUIReproductor(Cancion cancion) {
        reproductor.setVisibility(View.VISIBLE);
        txtTitulo.setText(cancion.getTitulo());
        image.setImageResource(cancion.getImagenResId());
        seekBar.setMax(mediaPlayer.getDuration());
        btnPlayPause.setImageResource(android.R.drawable.ic_media_pause);
        actualizarTiempoRestante();
    }

    // Alternar entre play y pause
    private void togglePlayPause() {
        if (mediaPlayer != null) {
            if (isPlaying) {
                mediaPlayer.pause();
                btnPlayPause.setImageResource(android.R.drawable.ic_media_play);
                isPlaying = false;
            } else {
                mediaPlayer.start();
                btnPlayPause.setImageResource(android.R.drawable.ic_media_pause);
                isPlaying = true;
                handler.post(updateSeekBar);
            }
        }
    }

    // Rebobinar
    private void rew() {
        if (mediaPlayer != null) {
            int currentPosition = mediaPlayer.getCurrentPosition();
            mediaPlayer.seekTo(Math.max(currentPosition - 5000, 0)); // Rebobina 5 segundos
        }
    }

    // Avanzar
    private void ff() {
        if (mediaPlayer != null) {
            int currentPosition = mediaPlayer.getCurrentPosition();
            mediaPlayer.seekTo(Math.min(currentPosition + 5000, mediaPlayer.getDuration())); // Avanza 5 segundos
        }
    }

    // Detener la reproducción
    private void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            isPlaying = false;
            btnPlayPause.setImageResource(android.R.drawable.ic_media_play);
            seekBar.setProgress(0);
            txtTiempoRestante.setText(formatoTiempo(mediaPlayer.getDuration()));
        }
    }

    // Reproducir la siguiente canción
    private void siguienteCancion() {
        if (cancionIndex < cancionList.size() - 1) {
            reproducirCancion(cancionList.get(cancionIndex + 1));
        }
    }

    // Reproducir la canción anterior
    private void anteriorCancion() {
        if (cancionIndex > 0) {
            reproducirCancion(cancionList.get(cancionIndex - 1));
        }
    }

    // Actualizar el tiempo restante
    private void actualizarTiempoRestante() {
        handler.postDelayed(() -> {
            if (mediaPlayer != null && isPlaying) {
                int tiempoRestante = mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition();
                txtTiempoRestante.setText(formatoTiempo(tiempoRestante));
                handler.postDelayed(this::actualizarTiempoRestante, 500);
            }
        }, 500);
    }

    // Formatear el tiempo (mm:ss)
    private String formatoTiempo(int millis) {
        return String.format(Locale.getDefault(), "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) % 60);
    }

    // Runnable para actualizar la SeekBar
    private Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null && isPlaying) {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 500);
            }
        }
    };
}