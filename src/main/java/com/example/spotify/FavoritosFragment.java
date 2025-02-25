package com.example.spotify;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FavoritosFragment extends Fragment {

    private RecyclerView recyclerView;
    private CancionAdapter favoritosAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favoritos, container, false);

        recyclerView = view.findViewById(R.id.recycler_favoritos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Log.i("favoritos","Lista de favoritos actualizada");
        // Obtener lista de favoritos desde el gestor central
        favoritosAdapter = new CancionAdapter(ClassList.listaFavoritos);
        recyclerView.setAdapter(favoritosAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Notificar cambios al adaptador cuando el fragmento se reanuda
        if (favoritosAdapter != null) {
            favoritosAdapter.notifyDataSetChanged();
        }
    }
}
