package com.eam.biblioteca.persistence;

import com.eam.biblioteca.core.Configuracion;
import com.eam.biblioteca.model.Cliente;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RepositorioClientes {
    private static final String ARCHIVO = "clientes.csv";
    private static final String ENCABEZADO = "documento,nombreCompleto,telefono,direccion,multasAcumuladas";

    private Path ruta() {
        return Paths.get(Configuracion.DIRECTORIO_DATOS, ARCHIVO);

        public void guardar(List<Cliente> clientes) {
            List<String> lineas = new ArrayList<>();
            lineas.add(ENCABEZADO);
            for (Cliente c : clientes) {
                lineas.add(Utilcsv.unir(
                        c.getDocumento(),
                        c.getNombreCompleto(),
                        c.getTelefono(),
                        c.getDireccion(),
                        String.valueOf(c.getMultasAcumuladas())));
            }
            try {
                Files.createDirectories(Paths.get(Configuracion.DIRECTORIO_DATOS));
                Files.write(ruta(), lineas, StandardCharsets.UTF_8);
            } catch (IOException e) {
                System.out.println("Error al guardar clientes: " + e.getMessage());
            }
        }

        public List<Cliente> cargar() {
            List<Cliente> clientes = new ArrayList<>();
            Path archivo = ruta();
            if (!Files.exists(archivo)) {
                return clientes;
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
                        clientes.add(new Cliente(
                                campos.get(0),
                                campos.get(1),
                                campos.get(2),
                                campos.get(3),
                                Double.parseDouble(campos.get(4))));
                    } catch (IndexOutOfBoundsException | NumberFormatException e) {
                        System.out.println("Aviso: línea inválida en clientes.csv (línea " + (i + 1) + "), se omite.");
                    }
                }
            } catch (IOException e) {
                System.out.println("Error al leer clientes: " + e.getMessage());
            }
            return clientes;
        }
}