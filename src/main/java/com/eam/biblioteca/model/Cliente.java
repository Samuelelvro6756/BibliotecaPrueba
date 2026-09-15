package com.eam.biblioteca.model;

/**
 * Representa a un cliente registrado en la biblioteca.
 * Responsable: Persona 2 (Clientes/Préstamos).
 */
public class Cliente {

    private String documento;
    private String nombreCompleto;
    private String telefono;
    private String direccion;
    private boolean tienePrestamoActivo;

    public Cliente(String documento, String nombreCompleto, String telefono, String direccion) {
        this.documento = documento;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.direccion = direccion;
        this.tienePrestamoActivo = false;
    }

    // ---------- Getters ----------
    public String getDocumento() { return documento; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getTelefono() { return telefono; }
    public String getDireccion() { return direccion; }
    public boolean isTienePrestamoActivo() { return tienePrestamoActivo; }

    /**
     * TODO (Persona 2): marcar que el cliente recibió un libro en préstamo.
     */
    public void marcarPrestamo() {
        this.tienePrestamoActivo=true;
    }

    /**
     * TODO (Persona 2): marcar que el cliente devolvió su libro
     * (queda libre para pedir otro).
     */
    public void marcarDevolucion() {
        this.tienePrestamoActivo=false;
    }

    /**
     * TODO (Persona 2): retornar toda la información del cliente
     * en formato de texto.
     */
    @Override
    public String toString() {
        return "Documento: " + documento +
                "\nNombre: " + nombreCompleto +
                "\nTeléfono: " + telefono +
                "\nDirección: " + direccion +
                "\nPréstamo activo: " + (tienePrestamoActivo ? "Sí" : "No");
    }
}
