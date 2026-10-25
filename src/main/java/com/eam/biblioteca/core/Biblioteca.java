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
    
    public boolean reservarLibro(String idLibro, String documentoCliente) {
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

        if (libro.getEstado() != EstadoLibro.PRESTADO) {
            System.out.println("Error: solo se pueden reservar libros que estén prestados.");
            return false;
        }

        if (documentoCliente.equals(prestamosActivos.get(idLibro))) {
            System.out.println("Error: el cliente ya tiene este libro en préstamo.");
            return false;
        }

        if (libro.getReservas().contains(documentoCliente)) {
            System.out.println("Error: el cliente ya reservó este libro.");
            return false;
        }

        libro.agregarReserva(documentoCliente);
        return true;
    }
    
    public List<Cliente> consultarReservas(String idLibro) {
        Libro libro = buscarLibroPorId(idLibro);
        if (libro == null) {
            System.out.println("Error: no existe un libro con el id " + idLibro);
            return null;
        }

        List<Cliente> cola = new ArrayList<>();
        for (String documento : libro.getReservas()) {
            Cliente cliente = buscarClientePorDocumento(documento);
            if (cliente != null) {
                cola.add(cliente);
            }
        }
        return cola;
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
        
        if (libro.tieneReservas() && !documentoCliente.equals(libro.siguienteEnCola())) {
            System.out.println("Error: el libro está reservado para otro cliente que va primero en la cola.");
            return false;
        }

        libro.cambiarEstado(EstadoLibro.PRESTADO);
        cliente.marcarPrestamo();
        prestamosActivos.put(idLibro, documentoCliente);
        
        if (libro.tieneReservas()) {
            libro.quitarPrimeraReserva();
        }
        
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
        
        if (libro.tieneReservas()) {
            String documentoSiguiente = libro.siguienteEnCola();
            Cliente siguiente = buscarClientePorDocumento(documentoSiguiente);
            String nombre = (siguiente != null) ? siguiente.getNombreCompleto() : documentoSiguiente;
            System.out.println("Aviso: este libro tiene reservas. Sigue en la cola: "
                    + nombre + " (documento " + documentoSiguiente + ")");
        }
        
        return true;
    }
}
