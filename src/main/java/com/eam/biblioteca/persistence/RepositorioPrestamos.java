package com.eam.biblioteca.persistence;

import com.eam.biblioteca.model.Prestamo;
import java.util.ArrayList;
import java.util.List;

public class RepositorioPrestamos {

    public void guardar(List<Prestamo> prestamos) {
        // TODO (Persona 2): escribir data/prestamos.csv
    }

    public List<Prestamo> cargar() {
        // TODO (Persona 2): leer data/prestamos.csv
        return new ArrayList<>();
    }
}