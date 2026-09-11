package com.eam.biblioteca;

import com.eam.biblioteca.core.Biblioteca;

import java.util.Scanner;

/**
 * Punto de entrada y menú principal por consola.
 * ARCHIVO COMPARTIDO: cada quien agrega su propio submenú en un
 * método separado para minimizar conflictos de merge.
 */
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
                case 2 -> mostrarMenuClientes();  // Persona 2
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("\n===== BIBLIOTECA LIBROSYMAS =====");
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

    // ---------- TODO (Persona 1 - Inventario) ----------
    private static void mostrarMenuLibros() {
        // TODO: submenú con "registrar libro" y "listar libros disponibles"
    }

    // ---------- TODO (Persona 2 - Clientes/Préstamos) ----------
    private static void mostrarMenuClientes() {
        // TODO: submenú con registrar cliente, prestar libro, devolver libro
    }
}
