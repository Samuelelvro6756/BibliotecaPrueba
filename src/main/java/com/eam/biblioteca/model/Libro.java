package com.eam.biblioteca.model;
import java.util.ArrayList;
import java.util.List;

public class Libro {

    private String id;
    private String titulo;
    private String autor;
    private String editorial;
    private int anioPublicacion;
    private Categoria categoria;
    private EstadoLibro estado;
    private List<String> reservas = new ArrayList<>();

    public Libro(String id, String titulo, String autor, String editorial,int anioPublicacion, Categoria categoria) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.anioPublicacion = anioPublicacion;
        this.categoria = categoria;
        this.estado = EstadoLibro.DISPONIBLE;
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getEditorial() { return editorial; }
    public int getAnioPublicacion() { return anioPublicacion; }
    public Categoria getCategoria() { return categoria; }
    public EstadoLibro getEstado() { return estado; }

    public void cambiarEstado(EstadoLibro nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public String toString() {
        return "ID: " + id + " | Título: " + titulo +
                " | Autor: " + autor + " | Editorial: " + editorial +
                " | Año: " + anioPublicacion + " | Categoría: " + categoria + " | Estado: " + estado;
    }

    public void setEstado(EstadoLibro estado) {
        this.estado = estado;
    }
    public void agregarReserva(String documento) {
        reservas.add(documento);
    }

    public List<String> getReservas() {
        return new ArrayList<>(reservas);
    }

    public boolean tieneReservas() {
        return !reservas.isEmpty();
    }

    public String siguienteEnCola() {
        return reservas.isEmpty() ? null : reservas.get(0);
    }

    public void quitarPrimeraReserva() {
        if (!reservas.isEmpty()) {
            reservas.remove(0);
        }
    }
}
