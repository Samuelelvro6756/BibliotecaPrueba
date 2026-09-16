package com.eam.biblioteca.core;

import com.eam.biblioteca.model.Cliente;
import com.eam.biblioteca.model.EstadoLibro;
import com.eam.biblioteca.model.Libro;
import com.eam.biblioteca.model.EstadoLibro;
import java.util.ArrayList;
import java.util.List;

public class Biblioteca {

    private List<Libro> libros = new ArrayList<>();
    private List<Cliente> clientes = new ArrayList<>();

    public void registrarLibro(Libro libro) {
        if (buscarLibroPorId(libro.getId()) != null) {
            System.out.println("Ya existe un libro registrado con el id " + libro.getId());
            return;
        }
        libros.add(libro);
    }

    public List<Libro> listarLibrosDisponibles() {
        List<Libro> disponibles = new ArrayList<>();
        for (Libro libro : libros) {
            if (libro.getEstado() == EstadoLibro.DISPONIBLE) {
                disponibles.add(libro);
            }
        }
        return disponibles;
    }
    
    private Libro buscarLibroPorId(String id) {
        for (Libro libro : libros) {
            if (libro.getId().equals(id)) {
                return libro;
            }
        }
        return null;
    }

    // ---------- Persona 2 (Clientes) ----------

    /** TODO: agregar un cliente nuevo a la lista de clientes. */
    public void registrarCliente(Cliente cliente) {
    if (cliente == null){
        System.out.println("el cliente no puede estar vacio");
    }
    boolean existe = clientes.stream()
            .anyMatch(c->c.getDocumento().equals(cliente.getDocumento()));
    if (existe){
        System.out.println("Error: ya existe un cliente con el documento"+ cliente.getDocumento());
        return;
    }
    clientes.add(cliente);
    System.out.println("Cliente: "+cliente.getNombreCompleto()+" fue registrado exitosamente");

    }

    // ---------- Lógica compartida (préstamos/devoluciones) ----------

    /**
     * TODO: validar que el libro exista y esté DISPONIBLE, y que el
     * cliente exista y no tenga ya un préstamo activo. Si todo es
     * correcto, registrar el préstamo (cambiar estado del libro,
     * marcar al cliente, guardar la fecha).
     */
    public boolean prestarLibro(String idLibro, String documentoCliente) {
        return false;
    }

    /**
     * TODO: validar que el libro exista y esté PRESTADO. Si es así,
     * volverlo DISPONIBLE y liberar al cliente asociado.
     */
    public boolean devolverLibro(String idLibro, String documentoCliente) {
        // TODO: implementar
        return false;
    }
}
