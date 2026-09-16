package com.eam.biblioteca.model;

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

    public void marcarPrestamo() {
        this.tienePrestamoActivo=true;
    }

    public void marcarDevolucion() {
        this.tienePrestamoActivo=false;
    }

    @Override
    public String toString() {
        return "Documento: " + documento +
                "\nNombre: " + nombreCompleto +
                "\nTeléfono: " + telefono +
                "\nDirección: " + direccion +
                "\nPréstamo activo: " + (tienePrestamoActivo ? "Sí" : "No");
    }
}
