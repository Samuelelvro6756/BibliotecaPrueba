package com.eam.biblioteca.persistence;

import com.eam.biblioteca.model.Cliente;
import java.util.ArrayList;
import java.util.List;

public class RepositorioClientes {

    public void guardar(List<Cliente> clientes) {
        // TODO (Persona 2): escribir data/clientes.csv
    }

    public List<Cliente> cargar() {
        // TODO (Persona 2): leer data/clientes.csv
        return new ArrayList<>();
    }
}