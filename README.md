# 📚 Biblioteca LibrosYMas

Sistema de gestión de biblioteca por consola, desarrollado en **Java** de forma colaborativa con **Git** para la Institución Universitaria EAM (Armenia, Colombia). Administra el inventario de libros, los clientes y los préstamos/devoluciones. En su segunda etapa incorpora multas, reservas, un reporte general y ejecución en **Docker** con persistencia de datos.

---

## Tabla de contenido

1. [Funcionalidades](#funcionalidades)
2. [Reglas de negocio](#reglas-de-negocio)
3. [Estructura del proyecto](#estructura-del-proyecto)
4. [Requisitos](#requisitos)
5. [Cómo ejecutarlo](#cómo-ejecutarlo)
6. [Ejecución con Docker](#ejecución-con-docker)
7. [Menú de la aplicación](#menú-de-la-aplicación)
8. [Flujo de trabajo con Git](#flujo-de-trabajo-con-git)
9. [Equipo y reparto del trabajo](#equipo-y-reparto-del-trabajo)

---

## Funcionalidades

**Etapa 1: implementado ✅**
- Registro de libros con id único, título, autor, editorial, año, categoría y estado.
- Listado de libros disponibles.
- Registro de clientes (documento, nombre, teléfono, dirección).
- Préstamo y devolución de libros con validaciones de consistencia.

**Etapa 2: en desarrollo 🔜**
- Registro de cada préstamo con fecha y fecha límite, más historial completo.
- **Multas** por retraso en la devolución, acumuladas en el perfil del cliente.
- **Reservas** de libros prestados, con cola por orden de solicitud y aviso al devolver.
- **Reporte general** (totales, préstamos activos e históricos, cliente con más préstamos, libro menos solicitado).
- **Persistencia** en archivos CSV (libros, clientes, préstamos, reservas).
- **Dockerización** con volumen para conservar los datos.

---

## Reglas de negocio

- Todo libro pertenece a una categoría: `LITERATURA`, `CIENCIA`, `HISTORIA` o `TECNOLOGIA`.
- Cada ejemplar tiene un **id único**, incluso si hay varios con el mismo título.
- Un libro está en uno de tres estados: `DISPONIBLE`, `PRESTADO` o `RETIRADO`.
- Cada cliente puede tener **un solo libro en préstamo** a la vez.
- Un préstamo solo se registra si el libro está disponible **y** el cliente no tiene otro préstamo activo.
- Una devolución solo se acepta si el libro figura como prestado a ese cliente.
- No se permiten ids de libro ni documentos de cliente duplicados.

---

## Estructura del proyecto

```
BibliotecaPrueba/
├── src/main/java/
│   ├── module-info.java
│   └── com/eam/biblioteca/
│       ├── Main.java                 # Punto de entrada y menús por consola
│       ├── core/
│       │   └── Biblioteca.java       # Lógica central: inventario, préstamos, devoluciones
│       └── model/
│           ├── Libro.java
│           ├── Categoria.java        # enum
│           ├── EstadoLibro.java      # enum
│           └── Cliente.java
├── .gitignore
└── README.md
```

> Se irán agregando `Prestamo`, los repositorios de persistencia (CSV), la carpeta `data/` y el `Dockerfile`.

---

## Requisitos

- **JDK 17 o superior** (el código usa `switch` con flechas, disponible desde Java 14).
- Un IDE (Eclipse o IntelliJ IDEA) o solo la terminal.
- **Docker** (opcional, para la etapa de contenedores).
- **Git**.

---

## Cómo ejecutarlo

### Desde el IDE
1. Clona el repositorio:
   ```bash
   git clone <url-del-repositorio>
   ```
2. Impórtalo como proyecto Java existente (Eclipse: *File → Import → Existing Projects*; IntelliJ: *Open*).
3. Ejecuta la clase `com.eam.biblioteca.Main`.

### Desde la terminal
```bash
# Compilar (desde la raíz del repositorio)
mkdir -p out
javac -d out $(find src/main/java/com -name "*.java")

# Ejecutar
java -cp out com.eam.biblioteca.Main
```

---

## Ejecución con Docker

> Esta sección se completará cuando esté listo el `Dockerfile`. El uso previsto es:

```bash
# Construir la imagen
docker build -t biblioteca-libros-y-mas .

# Ejecutar con un volumen para que los datos persistan
docker run -it --rm -v biblioteca-data:/app/data biblioteca-libros-y-mas
```

- `-it` es necesario porque la aplicación lee datos desde la consola.
- El volumen `biblioteca-data` guarda los CSV fuera del contenedor, de modo que la información sigue ahí aunque el contenedor se detenga o se elimine.

---

## Menú de la aplicación

```
===== BIBLIOTECA LIBROSYMAS =====
1. Gestión de libros
2. Gestión de clientes y préstamos
0. Salir
```

**Gestión de libros:** registrar libro · listar libros disponibles
**Gestión de clientes y préstamos:** registrar cliente · prestar libro · devolver libro

Próximamente: reservas, consulta de multas por cliente y reporte general.

---

## Flujo de trabajo con Git

- `main` contiene siempre la versión estable.
- Cada funcionalidad se desarrolla en una rama propia (`feature/...`) y se integra a `main` mediante **Pull Request** revisado por el otro integrante.
- Commits pequeños y con mensajes claros (por ejemplo: `Agrega clase Prestamo con fechas`).
- Antes de hacer `push`, se hace `git pull` para traer los cambios del compañero.
- Los archivos compartidos (`Main.java`, `Biblioteca.java`) se editan con coordinación previa.
- El `.gitignore` excluye los archivos de configuración de Eclipse e IntelliJ para que ambos IDE convivan sin conflictos.

---

## Equipo y reparto del trabajo

| Integrante | Rol | Responsabilidades |
|---|---|---|
| _Nombre 1_ | Libros e inventario | `Libro`, categorías y estados, reservas, persistencia de libros, reporte general, Docker |
| _Nombre 2_ | Clientes y préstamos | `Cliente`, `Prestamo` e historial, multas, persistencia de clientes y préstamos |

**Institución:** Institución Universitaria EAM, Armenia, Colombia
**Materia:** Desarrollo usando GIT