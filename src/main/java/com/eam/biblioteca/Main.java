package com.eam.biblioteca;

import com.eam.biblioteca.core.Biblioteca;
import com.eam.biblioteca.model.*;
import java.util.Scanner;
import java.util.List;

public class Main {

    private static Biblioteca biblioteca = new Biblioteca();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenuPrincipal();
            opcion = leerOpcion();
            switch (opcion) {
                case 1 -> mostrarMenuLibros();    // Persona 1
                case 2 -> mostrarMenuClientes();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("\n===== BIBLIOTECA LIBROS Y MAS =====");
        System.out.println("1. Gestión de libros");
        System.out.println("2. Gestión de clientes y préstamos");
        System.out.println("0. Salir");
        System.out.print("Elige una opción: ");
    }

    private static int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Inventario
    private static void mostrarMenuLibros() {
        int opcion;
        do {
            System.out.println("\n--- Gestión de libros ---");
            System.out.println("1. Registrar libro");
            System.out.println("2. Listar libros disponibles");
            System.out.println("0. Volver");
            System.out.print("Elige una opción: ");
            opcion = leerOpcion();
            switch (opcion) {
                case 1 -> registrarLibroDesdeConsola();
                case 2 -> listarLibrosDesdeConsola();
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private static void registrarLibroDesdeConsola() {
        try {
            System.out.print("ID del libro: ");
            String id = scanner.nextLine().trim();
            System.out.print("Título: ");
            String titulo = scanner.nextLine().trim();
            System.out.print("Autor: ");
            String autor = scanner.nextLine().trim();
            System.out.print("Editorial: ");
            String editorial = scanner.nextLine().trim();
            System.out.print("Año de publicación: ");
            int anio = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Categoría (LITERATURA, CIENCIA, HISTORIA, TECNOLOGIA): ");
            Categoria categoria = Categoria.valueOf(scanner.nextLine().trim().toUpperCase());

            Libro libro = new Libro(id, titulo, autor, editorial, anio, categoria);
            biblioteca.registrarLibro(libro);
            System.out.println("Libro registrado correctamente.");
        } catch (NumberFormatException e) {
            System.out.println("El año debe ser un número válido.");
        } catch (IllegalArgumentException e) {
            System.out.println("Categoría inválida. Usa: LITERATURA, CIENCIA, HISTORIA o TECNOLOGIA.");
        }
    }

    private static void listarLibrosDesdeConsola() {
        List<Libro> disponibles = biblioteca.listarLibrosDisponibles();
        if (disponibles.isEmpty()) {
            System.out.println("No hay libros disponibles.");
            return;
        }
        System.out.println("--- Libros disponibles ---");
        for (Libro libro : disponibles) {
            System.out.println(libro);
        }
    }

    // Clientes
    private static void mostrarMenuClientes() {
        int opcion;
        do {
            System.out.println("\n----- GESTIÓN DE CLIENTES Y PRÉSTAMOS -----");
            System.out.println("1. Registrar cliente");
            System.out.println("2. Prestar libro");
            System.out.println("3. Devolver libro");
            System.out.println("0. Volver al menú principal");
            System.out.print("Elige una opción: ");

            opcion = leerOpcion();

            switch (opcion) {
                case 1 -> registrarClienteDesdeMenu();
                case 2 -> prestarLibroDesdeMenu();
                case 3 -> devolverLibroDesdeMenu();
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }
    private static void registrarClienteDesdeMenu() {
        System.out.print("Documento: ");
        String documento = scanner.nextLine().trim();

        System.out.print("Nombre completo: ");
        String nombreCompleto = scanner.nextLine().trim();

        System.out.print("Teléfono: ");
        String telefono = scanner.nextLine().trim();

        System.out.print("Dirección: ");
        String direccion = scanner.nextLine().trim();

        Cliente cliente = new Cliente(documento, nombreCompleto, telefono, direccion);
        biblioteca.registrarCliente(cliente);
    }

    private static void prestarLibroDesdeMenu() {
        System.out.print("ID del libro: ");
        String idLibro = scanner.nextLine().trim();

        System.out.print("Documento del cliente: ");
        String documentoCliente = scanner.nextLine().trim();

        boolean exito = biblioteca.prestarLibro(idLibro, documentoCliente);

        if (exito) {
            System.out.println("Préstamo registrado correctamente.");
        } else {
            System.out.println("No se pudo registrar el préstamo.");
        }
    }

    private static void devolverLibroDesdeMenu() {
        System.out.print("ID del libro: ");
        String idLibro = scanner.nextLine().trim();

        System.out.print("Documento del cliente: ");
        String documentoCliente = scanner.nextLine().trim();

        boolean exito = biblioteca.devolverLibro(idLibro, documentoCliente);

        if (exito) {
            System.out.println("Devolución registrada correctamente.");
        } else {
            System.out.println("No se pudo registrar la devolución.");
        }
    }
}
