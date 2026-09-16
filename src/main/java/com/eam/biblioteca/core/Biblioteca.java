package com.eam.biblioteca.core;

import com.eam.biblioteca.model.Cliente;
import com.eam.biblioteca.model.EstadoLibro;
import com.eam.biblioteca.model.Libro;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Biblioteca {

    private List<Libro> libros = new ArrayList<>();
    private List<Cliente> clientes = new ArrayList<>();

    private Map<String, String> prestamosActivos = new HashMap<>();

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

    private Cliente buscarClientePorDocumento(String documento) {
        for (Cliente cliente : clientes) {
            if (cliente.getDocumento().equals(documento)) {
                return cliente;
            }
        }
        return null;
    }

    public boolean prestarLibro(String idLibro, String documentoCliente) {
        Libro libro = buscarLibroPorId(idLibro);
        if (libro == null) {
            System.out.println("Error: no existe un libro con el id " + idLibro);
            return false;
        }

        Cliente cliente = buscarClientePorDocumento(documentoCliente);
        if (cliente == null) {
            System.out.println("Error: no existe un cliente con el documento " + documentoCliente);
            return false;
        }

        if (libro.getEstado() != EstadoLibro.DISPONIBLE) {
            System.out.println("Error: el libro '" + libro.getTitulo() + "' no está disponible.");
            return false;
        }

        if (cliente.isTienePrestamoActivo()) {
            System.out.println("Error: el cliente " + cliente.getNombreCompleto() + " ya tiene un préstamo activo.");
            return false;
        }

        libro.cambiarEstado(EstadoLibro.PRESTADO);
        cliente.marcarPrestamo();
        prestamosActivos.put(idLibro, documentoCliente);
        return true;
    }

    public boolean devolverLibro(String idLibro, String documentoCliente) {
        Libro libro = buscarLibroPorId(idLibro);
        if (libro == null) {
            System.out.println("Error: no existe un libro con el id " + idLibro);
            return false;
        }

        if (libro.getEstado() != EstadoLibro.PRESTADO) {
            System.out.println("Error: el libro '" + libro.getTitulo() + "' no figura como prestado.");
            return false;
        }

        String documentoAsociado = prestamosActivos.get(idLibro);
        if (documentoAsociado == null || !documentoAsociado.equals(documentoCliente)) {
            System.out.println("Error: ese libro no fue prestado a este cliente.");
            return false;
        }

        Cliente cliente = buscarClientePorDocumento(documentoCliente);
        if (cliente != null) {
            cliente.marcarDevolucion();
        }

        libro.cambiarEstado(EstadoLibro.DISPONIBLE);
        prestamosActivos.remove(idLibro);
        return true;
    }
}
