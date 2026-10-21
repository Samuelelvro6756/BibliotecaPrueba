package com.eam.biblioteca.core;

public class Configuracion {

    private Configuracion() { }

    // Días que dura un préstamo antes de generar multa.
    public static int DIAS_PRESTAMO = 7;

    // Valor de la multa por cada día de retraso.
    public static double MULTA_POR_DIA = 1000.0;

    // Carpeta de datos.
    public static String DIRECTORIO_DATOS = System.getenv().getOrDefault("DATA_DIR", "data");
}