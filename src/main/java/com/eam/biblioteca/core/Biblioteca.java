package com.eam.biblioteca.core;

import com.eam.biblioteca.model.Cliente;
import com.eam.biblioteca.model.EstadoLibro;
import com.eam.biblioteca.model.Libro;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase central que centraliza la gestión del inventario de libros
 * y las operaciones de préstamo/devolución.
 *
 * ARCHIVO COMPARTIDO: ambos integrantes trabajan aquí. Coordínense
 * antes de modificarlo para evitar conflictos de merge (idealmente
 * cada uno implementa sus métodos en commits separados).
 */
public class Biblioteca {

    private List<Libro> libros = new ArrayList<>();
    private List<Cliente> clientes = new ArrayList<>();

    // ---------- Persona 1 (Inventario) ----------

    /** TODO: agregar un libro nuevo a la lista de libros. */
    public void registrarLibro(Libro libro) {
        // TODO: implementar
    }

    /** TODO: retornar los libros que estén en estado DISPONIBLE. */
    public List<Libro> listarLibrosDisponibles() {
        // TODO: implementar
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
        Libro libro = libros.stream()
                .filter(l->l.getId().equals(idLibro))
                .findFirst()
                .orElse(null);

        if (libro == null){
            System.out.println("No existe un libro con eL ID "+idLibro);
        }
        if (libro.getEstado() != EstadoLibro.DISPONIBLE){
            System.out.println("El libro "+libro.getTitulo()+" no se encuentra disponible o ya fue prestado");
        return false;
        }
        

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
