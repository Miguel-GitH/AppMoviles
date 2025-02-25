package com.example.spotify;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

// Clase utilizada para ver la lista de canciones
public class CancionAdapter extends RecyclerView.Adapter<CancionAdapter.CancionViewHolder> {

    private final List<Cancion> cancionList;
    private final CancionesFragment fragment; // Referencia al fragmento para reproducir canciones

    // Constructor con la lista de canciones y el fragmento para manejar la reproducción
    public CancionAdapter(List<Cancion> cancionList, CancionesFragment fragment) {
        this.cancionList = cancionList;
        this.fragment = fragment;
    }
    public CancionAdapter(List<Cancion> cancionList) {
        this.cancionList = cancionList;
        this.fragment = null;
    }

    @NonNull
    @Override
    public CancionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cancion, parent, false);
        return new CancionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CancionViewHolder holder, int position) {
        Cancion cancion = cancionList.get(position);

        holder.titulo.setText(cancion.getTitulo());
        holder.artista.setText(cancion.getArtista());
        holder.imagen.setImageResource(cancion.getImagenResId());

        // Configurar el ícono inicial según si la canción está en favoritos
        if (ClassList.listaFavoritos.contains(cancion)) {
            holder.favoritoButton.setImageResource(R.drawable.small_star_icon);
        } else {
            holder.favoritoButton.setImageResource(R.drawable.estrella_blanca);
        }

        // Evento de clic para agregar o quitar de favoritos
        holder.favoritoButton.setOnClickListener(v -> {
            if (ClassList.listaFavoritos.contains(cancion)) {
                ClassList.listaFavoritos.remove(cancion);
                holder.favoritoButton.setImageResource(R.drawable.estrella_blanca);
                Log.i("Favoritos", "Se eliminó de favoritos");
                Toast.makeText(v.getContext(), "Eliminado de Favoritos", Toast.LENGTH_SHORT).show();
            } else {
                ClassList.listaFavoritos.add(cancion);
                holder.favoritoButton.setImageResource(R.drawable.small_star_icon);
                Log.d("Favoritos", "Se añadió a favoritos");
                Toast.makeText(v.getContext(), "Añadido a Favoritos", Toast.LENGTH_SHORT).show();
            }
        });

        // Evento de clic para reproducir la canción
        holder.itemView.setOnClickListener(v -> {
            if (fragment != null) {
                fragment.reproducirCancion(cancion);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cancionList.size();
    }

    // Clase interna para los elementos de la vista
    public static class CancionViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, artista;
        ImageView imagen;
        ImageButton favoritoButton;

        public CancionViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.text_titulo);
            artista = itemView.findViewById(R.id.text_artista);
            imagen = itemView.findViewById(R.id.image_cancion);
            favoritoButton = itemView.findViewById(R.id.button_favorito);
        }
    }
}
