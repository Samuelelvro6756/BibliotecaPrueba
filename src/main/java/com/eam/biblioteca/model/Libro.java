package com.eam.biblioteca.model;

public class Libro {

    private String id;
    private String titulo;
    private String autor;
    private String editorial;
    private int anioPublicacion;
    private Categoria categoria;
    private EstadoLibro estado;

    public Libro(String id, String titulo, String autor, String editorial,int anioPublicacion, Categoria categoria) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.anioPublicacion = anioPublicacion;
        this.categoria = categoria;
        this.estado = EstadoLibro.DISPONIBLE; // todo libro nuevo inicia disponible
    }

    // ---------- Getters ----------
    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getEditorial() { return editorial; }
    public int getAnioPublicacion() { return anioPublicacion; }
    public Categoria getCategoria() { return categoria; }
    public EstadoLibro getEstado() { return estado; }

    public void cambiarEstado(EstadoLibro nuevoEstado) {
        // TODO: implementar
    }

    @Override
    public String toString() {
        // TODO: implementar
        return "";
    }
}
