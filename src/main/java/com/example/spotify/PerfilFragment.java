package com.example.spotify;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.spotify.R;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;

public class PerfilFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el diseño del fragmento
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        // Obtener referencias a los elementos del diseño
        TextView textViewGreeting = view.findViewById(R.id.textViewGreeting);
        ImageView imageViewProfile = view.findViewById(R.id.imageViewProfile);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        TextView textViewDate = view.findViewById(R.id.textView);
        textViewDate.setText("Seleccione una fecha: ");
        // Obtener el nombre del usuario desde los argumentos
        Bundle arguments = getArguments();
        if (arguments != null) {
            String username = arguments.getString("USERNAME_KEY");
            textViewGreeting.setText("Hola, " + username);
        }

        // Configurar la imagen predeterminada (opcional: puedes permitir que el usuario la cambie)
        imageViewProfile.setImageResource(R.drawable.profile);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    // Mostrar un mensaje o realizar una acción cuando cambie la calificación
                    Toast.makeText(getContext(), "Calificación: " + rating + " estrellas", Toast.LENGTH_SHORT).show();
                    reproducirSonido(R.raw.click2);
                }
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Formatear la fecha seleccionada
                String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
                textViewDate.setText("Fecha seleccionada: " + selectedDate);
                reproducirSonido(R.raw.click);
            }
        });
         return view;

    }
    private void reproducirSonido(int click) {
        MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), click);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
            }
        });
        mediaPlayer.start();
    }


}
