package com.eam.biblioteca.persistence;

import com.eam.biblioteca.model.Libro;
import java.util.ArrayList;
import java.util.List;

public class RepositorioLibros {

    public void guardar(List<Libro> libros) {
        // TODO (Persona 1): escribir data/libros.csv
    }

    public List<Libro> cargar() {
        // TODO (Persona 1): leer data/libros.csv
        return new ArrayList<>();
    }
}