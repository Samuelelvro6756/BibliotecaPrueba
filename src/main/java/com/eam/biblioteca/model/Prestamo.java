package com.eam.biblioteca.model;

import java.time.LocalDate;

public class Prestamo {

    private String idLibro;
    private String documentoCliente;
    private LocalDate fechaPrestamo;
    private LocalDate fechaLimite;
    private LocalDate fechaDevolucion;

    // Préstamo nuevo
    public Prestamo(String idLibro, String documentoCliente, LocalDate fechaPrestamo, LocalDate fechaLimite) {
        this(idLibro, documentoCliente, fechaPrestamo, fechaLimite, null);
    }

    // Constructor completo
    public Prestamo(String idLibro, String documentoCliente, LocalDate fechaPrestamo, LocalDate fechaLimite, LocalDate fechaDevolucion) {
        this.idLibro = idLibro;
        this.documentoCliente = documentoCliente;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaLimite = fechaLimite;
        this.fechaDevolucion = fechaDevolucion;
    }

    public String getIdLibro() { return idLibro; }
    public String getDocumentoCliente() { return documentoCliente; }
    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public LocalDate getFechaLimite() { return fechaLimite; }
    public LocalDate getFechaDevolucion() { return fechaDevolucion; }

    public boolean isActivo() {
        return fechaDevolucion == null;
    }

    public void registrarDevolucion(LocalDate fecha) {
        this.fechaDevolucion = fecha;
    }

    public String toString() {
        return "Libro: " + idLibro +
                " | Cliente: " + documentoCliente +
                " | Prestado: " + fechaPrestamo +
                " | Límite: " + fechaLimite +
                " | Devuelto: " + (fechaDevolucion == null ? "pendiente" : fechaDevolucion);
    }
}