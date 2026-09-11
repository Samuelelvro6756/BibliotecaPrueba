# Biblioteca LibrosYMas

Esqueleto inicial acordado entre los dos integrantes antes de dividir el
trabajo. Este debe ser el **primer commit** en `main`, antes de crear las
ramas `feature/libros` y `feature/clientes`.

## Estructura

```
src/main/java/com/eam/biblioteca/
├── Main.java                 (COMPARTIDO - menú principal)
├── core/
│   └── Biblioteca.java       (COMPARTIDO - centraliza inventario y préstamos)
└── model/
    ├── Libro.java            (Persona 1 - Inventario)
    ├── Categoria.java        (Persona 1 - Inventario)
    ├── EstadoLibro.java      (Persona 1 - Inventario)
    └── Cliente.java          (Persona 2 - Clientes/Préstamos)
```

## Siguientes pasos

1. Suban esta estructura a `main` como primer commit.
2. Cada uno crea su rama: `git checkout -b feature/libros` o
   `git checkout -b feature/clientes`.
3. Implementen los métodos marcados con `TODO` que sean de su
   responsabilidad.
4. Los archivos `Main.java` y `Biblioteca.java` son compartidos:
   coordínense antes de editarlos y avisen cuando vayan a hacer push
   sobre ellos.
5. Abran Pull Request hacia `main` cuando terminen cada parte, y
   revísense el código mutuamente antes de mergear.
