package com.eam.biblioteca.core;

import com.eam.biblioteca.model.Cliente;
import com.eam.biblioteca.model.EstadoLibro;
import com.eam.biblioteca.model.Libro;
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

    public boolean prestarLibro(String idLibro, String documentoCliente) {
        return false;
    }


    public boolean devolverLibro(String idLibro, String documentoCliente) {
        // TODO: implementar
        return false;
    }
}
