package com.eam.biblioteca.model;

public class Cliente {

    private String documento;
    private String nombreCompleto;
    private String telefono;
    private String direccion;
    private boolean tienePrestamoActivo;
    private double multasAcumuladas;

    public Cliente(String documento, String nombreCompleto, String telefono, String direccion) {
        this(documento, nombreCompleto, telefono, direccion, 0.0);
    }

    public Cliente(String documento, String nombreCompleto, String telefono, String direccion, double multasAcumuladas) {
        this.documento = documento;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.direccion = direccion;
        this.tienePrestamoActivo = false;
        this.multasAcumuladas = multasAcumuladas;
    }

    public String getDocumento() { return documento; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getTelefono() { return telefono; }
    public String getDireccion() { return direccion; }
    public boolean isTienePrestamoActivo() { return tienePrestamoActivo; }
    public double getMultasAcumuladas() { return multasAcumuladas; }

    public void marcarPrestamo() {
        this.tienePrestamoActivo=true;
    }

    public void marcarDevolucion() {
        this.tienePrestamoActivo=false;
    }

    public void agregarMulta(double monto) {
        if (monto > 0) {
            this.multasAcumuladas += monto;
        }
    }

    @Override
    public String toString() {
        return "Documento: " + documento +
                "\nNombre: " + nombreCompleto +
                "\nTeléfono: " + telefono +
                "\nDirección: " + direccion +
                "\nPréstamo activo: " + (tienePrestamoActivo ? "Sí" : "No") +
                "\nMultas acumuladas: $" + String.format("%.0f", multasAcumuladas);
    }
}
