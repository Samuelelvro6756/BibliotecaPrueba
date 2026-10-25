package com.eam.biblioteca.core;

import com.eam.biblioteca.model.Cliente;
import com.eam.biblioteca.model.EstadoLibro;
import com.eam.biblioteca.model.Libro;
import com.eam.biblioteca.model.Prestamo;
import com.eam.biblioteca.persistence.RepositorioClientes;
import com.eam.biblioteca.persistence.RepositorioLibros;
import com.eam.biblioteca.persistence.RepositorioPrestamos;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Biblioteca {

    private List<Libro> libros = new ArrayList<>();
    private List<Cliente> clientes = new ArrayList<>();
    private List<Prestamo> historialPrestamos = new ArrayList<>();

    private Map<String, String> prestamosActivos = new HashMap<>();
    private final RepositorioLibros repositorioLibros = new RepositorioLibros();
    private final RepositorioClientes repositorioClientes = new RepositorioClientes();
    private final RepositorioPrestamos repositorioPrestamos = new RepositorioPrestamos();

    public Biblioteca(){
        cargarDatos();
    }

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
    public Cliente obtenerCliente(String documento) {
        return buscarClientePorDocumento(documento);
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

        LocalDate hoy = LocalDate.now();
        Prestamo prestamo = new Prestamo(idLibro, documentoCliente, hoy, hoy.plusDays(Configuracion.DIAS_PRESTAMO));
        historialPrestamos.add(prestamo);

        libro.cambiarEstado(EstadoLibro.PRESTADO);
        cliente.marcarPrestamo();
        prestamosActivos.put(idLibro, documentoCliente);
        System.out.println("Fecha límite de devolución: " + prestamo.getFechaLimite());
        guardarDatos();
        return true;
    }
    public boolean devolverLibro(String idLibro, String documentoCliente) {
        return devolverLibro(idLibro, documentoCliente, LocalDate.now());
    }

    public boolean devolverLibro(String idLibro, String documentoCliente, LocalDate fechaDevolucion) {
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

        Prestamo prestamo = buscarPrestamoActivo(idLibro);
        if (prestamo == null) {
            System.out.println("Error: no se encontró el registro del préstamo de este libro.");
            return false;
        }

        if (fechaDevolucion.isBefore(prestamo.getFechaPrestamo())) {
            System.out.println("Error: la fecha de devolución no puede ser anterior a la fecha del préstamo ("
                    + prestamo.getFechaPrestamo() + ").");
            return false;
        }

        Cliente cliente = buscarClientePorDocumento(documentoCliente);

        prestamo.registrarDevolucion(fechaDevolucion);
        long diasRetraso = prestamo.getDiasRetraso();
        if (diasRetraso > 0) {
            double multa = diasRetraso * Configuracion.MULTA_POR_DIA;
            if (cliente != null) {
                cliente.agregarMulta(multa);
            }
            System.out.println("El libro se devolvió con " + diasRetraso + " día(s) de retraso. "
                    + "Multa generada: $" + String.format("%.0f", multa));
        }

        if (cliente != null) {
            cliente.marcarDevolucion();
        }

        libro.cambiarEstado(EstadoLibro.DISPONIBLE);
        prestamosActivos.remove(idLibro);
        guardarDatos();
        return true;
    }
    private Prestamo buscarPrestamoActivo(String idLibro) {
        for (Prestamo p : historialPrestamos) {
            if (p.getIdLibro().equals(idLibro) && p.isActivo()) {
                return p;
            }
        }
        return null;
    }

    //Historial

    public List<Prestamo> obtenerHistorial() {
        return new ArrayList<>(historialPrestamos);
    }

    public List<Prestamo> obtenerHistorialCliente(String documentoCliente) {
        List<Prestamo> resultado = new ArrayList<>();
        for (Prestamo p : historialPrestamos) {
            if (p.getDocumentoCliente().equals(documentoCliente)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    //Persistencia

    private void cargarDatos() {
        libros = new ArrayList<>(repositorioLibros.cargar());
        clientes = new ArrayList<>(repositorioClientes.cargar());
        historialPrestamos = new ArrayList<>(repositorioPrestamos.cargar());
        reconstruirPrestamosActivos();
    }
    // Restablecer el estado de libros y clientes a partir de los préstamos que siguen activos.
    private void reconstruirPrestamosActivos() {
        for (Prestamo p : historialPrestamos) {
            if (!p.isActivo()) {
                continue;
            }
            Libro libro = buscarLibroPorId(p.getIdLibro());
            Cliente cliente = buscarClientePorDocumento(p.getDocumentoCliente());
            if (libro == null || cliente == null) {
                System.out.println("Aviso: el préstamo activo del libro " + p.getIdLibro()
                        + " no se pudo asociar (libro o cliente no encontrado).");
                continue;
            }
            libro.cambiarEstado(EstadoLibro.PRESTADO);
            cliente.marcarPrestamo();
            prestamosActivos.put(p.getIdLibro(), p.getDocumentoCliente());
        }
    }

    private void guardarDatos() {
        repositorioLibros.guardar(libros);
        repositorioClientes.guardar(clientes);
        repositorioPrestamos.guardar(historialPrestamos);
    }
}
