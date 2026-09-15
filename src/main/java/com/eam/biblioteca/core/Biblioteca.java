package com.eam.biblioteca.core;

import com.eam.biblioteca.model.Cliente;
import com.eam.biblioteca.model.Libro;

import java.util.ArrayList;
import java.util.List;

public class Biblioteca {

    private List<Libro> libros = new ArrayList<>();
    private List<Cliente> clientes = new ArrayList<>();

    // Agregar un libro nuevo a la lista de libros.
    public void registrarLibro(Libro libro) {
        // TODO: implementar
    }

    // Retornar los libros que estén en estado DISPONIBLE.
    public List<Libro> listarLibrosDisponibles() {
        // TODO: implementar
        return null;
    }

    // ---------- Persona 2 (Clientes) ----------

    /** TODO: agregar un cliente nuevo a la lista de clientes. */
    public void registrarCliente(Cliente cliente) {
        // TODO: implementar
    }

    // ---------- Lógica compartida (préstamos/devoluciones) ----------

    /**
     * TODO: validar que el libro exista y esté DISPONIBLE, y que el
     * cliente exista y no tenga ya un préstamo activo. Si todo es
     * correcto, registrar el préstamo (cambiar estado del libro,
     * marcar al cliente, guardar la fecha).
     */
    public boolean prestarLibro(String idLibro, String documentoCliente) {
        // TODO: implementar
        return false;
    }

    /**
     * TODO: validar que el libro exista y esté PRESTADO. Si es así,
     * volverlo DISPONIBLE y liberar al cliente asociado.
     */
    public boolean devolverLibro(String idLibro) {
        // TODO: implementar
        return false;
    }
}
