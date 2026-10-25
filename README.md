# 📚 Biblioteca LibrosYMas

Sistema de gestión de biblioteca por consola, desarrollado en **Java** de forma colaborativa con **Git** para la Institución Universitaria EAM (Armenia, Colombia). Administra el inventario de libros, los clientes, los préstamos y devoluciones, las multas por retraso y las reservas. Los datos se guardan en archivos CSV y la aplicación se puede ejecutar en **Docker** conservando la información.

---

## Tabla de contenido

1. [Funcionalidades](#funcionalidades)
2. [Reglas de negocio](#reglas-de-negocio)
3. [Estructura del proyecto](#estructura-del-proyecto)
4. [Requisitos](#requisitos)
5. [Cómo ejecutarlo](#cómo-ejecutarlo)
6. [Ejecución con Docker](#ejecución-con-docker)
7. [Persistencia de datos](#persistencia-de-datos)
8. [Menú de la aplicación](#menú-de-la-aplicación)
9. [Flujo de trabajo con Git](#flujo-de-trabajo-con-git)
10. [Equipo y reparto del trabajo](#equipo-y-reparto-del-trabajo)

---

## Funcionalidades

- ✅ Registro de libros con id único, título, autor, editorial, año, categoría y estado.
- ✅ Listado de libros disponibles.
- ✅ Registro de clientes (documento, nombre, teléfono, dirección).
- ✅ Préstamo y devolución con validaciones de consistencia.
- ✅ Registro de cada préstamo con fecha y fecha límite, e historial (general y por cliente).
- ✅ **Multas** por retraso en la devolución, acumuladas en el perfil del cliente y consultables desde el menú.
- ✅ **Reservas** de libros prestados, con cola por orden de solicitud y aviso al devolver.
- ✅ **Reporte general**: totales, préstamos activos e históricos, cliente con más préstamos y libro menos solicitado.
- ✅ **Persistencia** en archivos CSV: los datos se conservan al cerrar el programa.
- ✅ **Docker** con volumen para conservar los datos entre ejecuciones del contenedor.

---

## Reglas de negocio

- Todo libro pertenece a una categoría: `LITERATURA`, `CIENCIA`, `HISTORIA` o `TECNOLOGIA`.
- Cada ejemplar tiene un **id único**, incluso si hay varios con el mismo título.
- Un libro está en uno de tres estados: `DISPONIBLE`, `PRESTADO` o `RETIRADO`.
- Cada cliente puede tener **un solo libro en préstamo** a la vez.
- Un préstamo solo se registra si el libro está disponible **y** el cliente no tiene otro préstamo activo.
- Una devolución solo se acepta si el libro figura como prestado a ese cliente.
- No se permiten ids de libro ni documentos de cliente duplicados.
- **Plazo y multa:** el préstamo dura 7 días; cada día de retraso genera una multa de $1.000 (valores en `Configuracion.java`).
- **Reservas:** solo se puede reservar un libro que esté prestado. Un cliente no puede reservar un libro que ya tiene ni reservarlo dos veces. Al devolverse, el libro solo puede prestarse al primero de la cola.

---

## Estructura del proyecto

```
BibliotecaPrueba/
├── src/main/java/
│   ├── module-info.java
│   └── com/eam/biblioteca/
│       ├── Main.java                  # Punto de entrada y menús por consola
│       ├── core/
│       │   ├── Biblioteca.java        # Lógica central: inventario, préstamos, reservas, reporte
│       │   └── Configuracion.java     # Plazo, multa por día y carpeta de datos
│       ├── model/
│       │   ├── Libro.java
│       │   ├── Cliente.java
│       │   ├── Prestamo.java
│       │   ├── Categoria.java         # enum
│       │   └── EstadoLibro.java       # enum
│       └── persistence/
│           ├── RepositorioLibros.java
│           ├── RepositorioClientes.java
│           ├── RepositorioPrestamos.java
│           └── Utilcsv.java
├── data/                              # Archivos CSV generados por la aplicación
├── Dockerfile
├── .dockerignore
├── .gitignore
└── README.md
```

---

## Requisitos

- **JDK 17 o superior**.
- Un IDE (Eclipse o IntelliJ IDEA) o solo la terminal.
- **Docker** (opcional, para ejecutar en contenedor).
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
javac -encoding UTF-8 -d out $(find src/main/java -name "*.java")

# Ejecutar
java -cp out com.eam.biblioteca.Main
```

---

## Ejecución con Docker

La imagen compila el código y ejecuta la aplicación sin ninguna configuración manual.

```bash
# 1. Construir la imagen (desde la raíz del repositorio)
docker build -t biblioteca-libros-y-mas .

# 2. Ejecutar con un volumen para que los datos persistan
docker run -it --rm -v biblioteca-data:/app/data biblioteca-libros-y-mas
```

- `-it` es necesario porque la aplicación lee datos desde la consola.
- `--rm` elimina el contenedor al salir. Los datos **no se pierden**, porque viven en el volumen `biblioteca-data`.
- La variable de entorno `DATA_DIR` (definida en la imagen como `/app/data`) indica dónde se guardan los CSV.

### Comprobar que los datos persisten
1. Ejecuta el contenedor, registra un libro y un cliente, y sal con la opción `0`.
2. Vuelve a ejecutar el mismo `docker run`.
3. Los datos siguen ahí, aunque el contenedor anterior ya fue eliminado.

### Otros comandos útiles
```bash
# Ver el volumen
docker volume inspect biblioteca-data

# Borrar todos los datos (empezar desde cero)
docker volume rm biblioteca-data

# Usar una carpeta local en lugar de un volumen (para ver los CSV directamente)
docker run -it --rm -v "$(pwd)/data:/app/data" biblioteca-libros-y-mas
```
En PowerShell, la última opción se escribe con `${PWD}/data` en lugar de `$(pwd)/data`.

---

## Persistencia de datos

La aplicación guarda automáticamente cada cambio y carga los datos al iniciar. Archivos dentro de la carpeta `data/`:

| Archivo | Contenido |
|---|---|
| `libros.csv` | Libros, su estado y la cola de reservas |
| `clientes.csv` | Clientes y multas acumuladas |
| `prestamos.csv` | Historial de préstamos (activos y devueltos) |

Al iniciar, el sistema reconstruye los préstamos activos a partir del historial. Los `.csv` están en el `.gitignore` para no subir datos de prueba al repositorio.

---

## Menú de la aplicación

```
===== BIBLIOTECA LIBROS Y MAS =====
1. Gestión de libros
2. Gestión de clientes y préstamos
3. Reporte general
0. Salir
```

**Gestión de libros:** registrar libro · listar libros disponibles · reservar libro · consultar reservas de un libro

**Gestión de clientes y préstamos:** registrar cliente · prestar libro · devolver libro · consultar multas de un cliente · ver historial de un cliente · ver historial general

**Reporte general:** total de libros y clientes, préstamos activos e históricos, cliente con más préstamos y libro menos solicitado (en caso de empate se muestran todos).

---

## Flujo de trabajo con Git

- `main` contiene siempre la versión estable.
- Cada funcionalidad se desarrolla en una rama propia (`feature/...`) y se integra a `main` mediante merge o Pull Request.
- Commits pequeños y con mensajes claros.
- Antes de hacer `push`, se hace `git pull` para traer los cambios del compañero.
- Los archivos compartidos (`Main.java`, `Biblioteca.java`) se editan con coordinación previa; los conflictos de merge se resolvieron conservando la lógica de ambos.
- El `.gitignore` excluye los archivos de configuración de Eclipse e IntelliJ para que ambos IDE convivan sin conflictos.

---

## Equipo y reparto del trabajo

| Integrante | Rol | Responsabilidades |
|---|---|---|
| _Nombre 1_ | Libros e inventario | `Libro`, categorías y estados, reservas, persistencia de libros, reporte general, Docker |
| _Nombre 2_ | Clientes y préstamos | `Cliente`, `Prestamo` e historial, multas, persistencia de clientes y préstamos |

**Institución:** Institución Universitaria EAM, Armenia, Colombia
**Materia:** Desarrollo usando GIT