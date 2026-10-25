package com.eam.biblioteca.persistence;

import com.eam.biblioteca.core.Configuracion;
import com.eam.biblioteca.model.Categoria;
import com.eam.biblioteca.model.EstadoLibro;
import com.eam.biblioteca.model.Libro;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RepositorioLibros {

    private static final String SEPARADOR = ";";

    private Path archivo() {
        return Paths.get(Configuracion.DIRECTORIO_DATOS, "libros.csv");
    }

    public void guardar(List<Libro> libros) {
        List<String> lineas = new ArrayList<>();
        for (Libro libro : libros) {
            lineas.add(String.join(SEPARADOR,
                    limpiar(libro.getId()),
                    limpiar(libro.getTitulo()),
                    limpiar(libro.getAutor()),
                    limpiar(libro.getEditorial()),
                    String.valueOf(libro.getAnioPublicacion()),
                    libro.getCategoria().name(),
                    libro.getEstado().name(),
                    String.join(",", libro.getReservas())));
        }
        try {
            Files.createDirectories(archivo().getParent());
            Files.write(archivo(), lineas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Error al guardar los libros: " + e.getMessage());
        }
    }

    public List<Libro> cargar() {
        List<Libro> libros = new ArrayList<>();
        if (!Files.exists(archivo())) {
            return libros;
        }
        try {
            for (String linea : Files.readAllLines(archivo(), StandardCharsets.UTF_8)) {
                if (linea.isBlank()) {
                    continue;
                }
                String[] c = linea.split(SEPARADOR, -1);
                if (c.length < 8) {
                    continue;
                }
                Libro libro = new Libro(c[0], c[1], c[2], c[3],
                        Integer.parseInt(c[4]), Categoria.valueOf(c[5]));
                libro.cambiarEstado(EstadoLibro.valueOf(c[6]));
                if (!c[7].isEmpty()) {
                    for (String documento : c[7].split(",")) {
                        libro.agregarReserva(documento);
                    }
                }
                libros.add(libro);
            }
        } catch (IOException | RuntimeException e) {
            System.out.println("Error al cargar los libros: " + e.getMessage());
        }
        return libros;
    }

    private String limpiar(String texto) {
        return texto.replace(";", ",");
    }
}