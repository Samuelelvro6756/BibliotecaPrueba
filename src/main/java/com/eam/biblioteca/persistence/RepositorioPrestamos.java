package com.eam.biblioteca.persistence;

import com.eam.biblioteca.core.Configuracion;
import com.eam.biblioteca.model.Prestamo;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class RepositorioPrestamos {
    private static final String ARCHIVO = "prestamos.csv";
    private static final String ENCABEZADO = "idLibro,documentoCliente,fechaPrestamo,fechaLimite,fechaDevolucion";

    private Path ruta() {
        return Paths.get(Configuracion.DIRECTORIO_DATOS, ARCHIVO);
    }

    public void guardar(List<Prestamo> prestamos) {
        List<String> lineas = new ArrayList<>();
        lineas.add(ENCABEZADO);
        for (Prestamo p : prestamos) {
            lineas.add(Utilcsv.unir(
                    p.getIdLibro(),
                    p.getDocumentoCliente(),
                    p.getFechaPrestamo().toString(),
                    p.getFechaLimite().toString(),
                    p.getFechaDevolucion() == null ? "" : p.getFechaDevolucion().toString()));
        }
        try {
            Files.createDirectories(Paths.get(Configuracion.DIRECTORIO_DATOS));
            Files.write(ruta(), lineas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Error al guardar préstamos: " + e.getMessage());
        }
    }

    public List<Prestamo> cargar() {
        List<Prestamo> prestamos = new ArrayList<>();
        Path archivo = ruta();
        if (!Files.exists(archivo)) {
            return prestamos;
        }
        try {
            List<String> lineas = Files.readAllLines(archivo, StandardCharsets.UTF_8);
            for (int i = 1; i < lineas.size(); i++) { // la línea 0 es el encabezado
                String linea = lineas.get(i);
                if (linea.isBlank()) {
                    continue;
                }
                try {
                    List<String> campos = Utilcsv.separar(linea);
                    LocalDate devolucion = campos.get(4).isBlank() ? null : LocalDate.parse(campos.get(4));
                    prestamos.add(new Prestamo(
                            campos.get(0),
                            campos.get(1),
                            LocalDate.parse(campos.get(2)),
                            LocalDate.parse(campos.get(3)),
                            devolucion));
                } catch (IndexOutOfBoundsException | DateTimeParseException e) {
                    System.out.println("Aviso: línea inválida en prestamos.csv (línea " + (i + 1) + "), se omite.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer préstamos: " + e.getMessage());
        }
        return prestamos;
    }
}