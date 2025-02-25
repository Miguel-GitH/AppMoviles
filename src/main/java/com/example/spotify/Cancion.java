package com.example.spotify;

public class Cancion {
    private String titulo;
    private String artista;
    private int imagenResId;
    private int audioResId;

    public Cancion(String titulo, String artista, int imagenResId, int audioResId) {
        this.titulo = titulo;
        this.artista = artista;
        this.imagenResId = imagenResId;
        this.audioResId = audioResId;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getArtista() {
        return artista;
    }

    public int getImagenResId() {
        return imagenResId;
    }

    public int getAudioResId() {
        return audioResId;
    }
}
