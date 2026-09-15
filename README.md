## Estructura del proyecto

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
