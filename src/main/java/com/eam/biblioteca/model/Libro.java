package com.eam.biblioteca.model;

/**
 * Representa un ejemplar dentro del inventario de la biblioteca.
 * Responsable: Persona 1 (Inventario).
 */
public class Libro {

    private String id;
    private String titulo;
    private String autor;
    private String editorial;
    private int anioPublicacion;
    private Categoria categoria;
    private EstadoLibro estado;

    public Libro(String id, String titulo, String autor, String editorial,
                 int anioPublicacion, Categoria categoria) {
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

    /**
     * TODO (Persona 1): cambiar el estado del libro (ej. a PRESTADO,
     * DISPONIBLE o RETIRADO).
     */
    public void cambiarEstado(EstadoLibro nuevoEstado) {
        // TODO: implementar
    }

    /**
     * TODO (Persona 1): retornar toda la información del libro
     * en formato de texto (id, título, autor, editorial, año,
     * categoría y estado).
     */
    @Override
    public String toString() {
        // TODO: implementar
        return "";
    }

    public void setEstado(EstadoLibro estado) {
        this.estado = estado;
    }
}
