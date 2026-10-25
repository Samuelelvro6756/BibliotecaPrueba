package com.eam.biblioteca.core;

import com.eam.biblioteca.model.Cliente;
import com.eam.biblioteca.persistence.RepositorioLibros;
import com.eam.biblioteca.model.EstadoLibro;
import com.eam.biblioteca.model.Libro;
import com.eam.biblioteca.model.Prestamo;
import com.eam.biblioteca.persistence.RepositorioClientes;
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

    public Biblioteca() {
        cargarDatos();
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
    
    public void registrarLibro(Libro libro) {
        if (buscarLibroPorId(libro.getId()) != null) {
            System.out.println("Ya existe un libro registrado con el id " + libro.getId());
            return;
        }
        libros.add(libro);
        guardarDatos();
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
        guardarDatos();
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

        LocalDate hoy = LocalDate.now();
        Prestamo prestamo = new Prestamo(idLibro, documentoCliente, hoy, hoy.plusDays(Configuracion.DIAS_PRESTAMO));
        historialPrestamos.add(prestamo);

        libro.cambiarEstado(EstadoLibro.PRESTADO);
        cliente.marcarPrestamo();
        prestamosActivos.put(idLibro, documentoCliente);
        prestamosActivos.put(idLibro, documentoCliente);
        System.out.println("Fecha límite de devolución: " + prestamo.getFechaLimite());

        if (libro.tieneReservas()) {
            libro.quitarPrimeraReserva();
        }

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
        libro.cambiarEstado(EstadoLibro.DISPONIBLE);
        prestamosActivos.remove(idLibro);

        if (libro.tieneReservas()) {
            String documentoSiguiente = libro.siguienteEnCola();
            Cliente siguiente = buscarClientePorDocumento(documentoSiguiente);
            String nombre = (siguiente != null) ? siguiente.getNombreCompleto() : documentoSiguiente;
            System.out.println("Aviso: este libro tiene reservas. Sigue en la cola: "
                    + nombre + " (documento " + documentoSiguiente + ")");
        }

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
    
    // Reporte
    public String generarReporte() {
        long activos = 0;
        long historicos = 0;
        Map<String, Integer> prestamosPorCliente = new HashMap<>();
        Map<String, Integer> prestamosPorLibro = new HashMap<>();

        for (Prestamo p : historialPrestamos) {
            if (p.isActivo()) {
                activos++;
            } else {
                historicos++;
            }
            prestamosPorCliente.merge(p.getDocumentoCliente(), 1, Integer::sum);
            prestamosPorLibro.merge(p.getIdLibro(), 1, Integer::sum);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Total de libros: ").append(libros.size()).append("\n");
        sb.append("Total de clientes: ").append(clientes.size()).append("\n");
        sb.append("Préstamos activos: ").append(activos).append("\n");
        sb.append("Préstamos históricos (devueltos): ").append(historicos).append("\n");
        sb.append("Cliente con más préstamos: ").append(clientesConMasPrestamos(prestamosPorCliente)).append("\n");
        sb.append("Libro menos solicitado: ").append(librosMenosSolicitados(prestamosPorLibro));
        return sb.toString();
    }

    private String clientesConMasPrestamos(Map<String, Integer> conteo) {
        int max = 0;
        for (int n : conteo.values()) {
            if (n > max) {
                max = n;
            }
        }
        if (max == 0) {
            return "aún no hay préstamos registrados";
        }
        List<String> nombres = new ArrayList<>();
        for (Cliente c : clientes) {
            if (conteo.getOrDefault(c.getDocumento(), 0) == max) {
                nombres.add(c.getNombreCompleto() + " (documento " + c.getDocumento() + ")");
            }
        }
        return String.join(", ", nombres) + " con " + max + " préstamo(s)";
    }

    private String librosMenosSolicitados(Map<String, Integer> conteo) {
        if (libros.isEmpty()) {
            return "no hay libros registrados";
        }
        if (historialPrestamos.isEmpty()) {
            return "aún no hay préstamos registrados";
        }
        int min = Integer.MAX_VALUE;
        for (Libro l : libros) {
            min = Math.min(min, conteo.getOrDefault(l.getId(), 0));
        }
        List<String> titulos = new ArrayList<>();
        for (Libro l : libros) {
            if (conteo.getOrDefault(l.getId(), 0) == min) {
                titulos.add(l.getTitulo() + " (id " + l.getId() + ")");
            }
        }
        return String.join(", ", titulos) + " con " + min + " préstamo(s)";
    }
    
    private void cargarDatos() {
        libros = new ArrayList<>(repositorioLibros.cargar());
        clientes = new ArrayList<>(repositorioClientes.cargar());
        historialPrestamos = new ArrayList<>(repositorioPrestamos.cargar());
        reconstruirPrestamosActivos();
    }

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
