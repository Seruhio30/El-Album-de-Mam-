# El Álbum de Mamá

Aplicación web sencilla y accesible para visualizar fotografías y videos familiares.

## Objetivo

Permitir que una adulta mayor pueda navegar por sus recuerdos de forma clara, intuitiva y segura.

## Estado actual

El proyecto cuenta con una galería dinámica para visualizar fotografías y videos familiares.

Actualmente incluye:

* Filtros por Todos, Viajes, Familia y Celebraciones.
* Visor interno para fotografías.
* Reproductor interno para videos.
* Diseño responsive y accesible.
* JavaScript organizado en módulos.
* CSS dividido en estilos base, layout y componentes.
* Datos cargados actualmente desde `frontend/data/memories.json`.
* Backend inicial desarrollado con Java y Spring Boot.
* API REST de solo lectura para consultar recuerdos.
* Endpoint de salud para comprobar el estado del backend.
* Pruebas automatizadas para los endpoints de recuerdos.

La aplicación está enfocada únicamente en la visualización de recuerdos. En esta etapa del MVP, la usuaria no puede subir, editar ni eliminar archivos.

El frontend todavía no está conectado con el backend.

## Tecnologías

### Frontend

* HTML.
* CSS.
* JavaScript nativo.
* JSON local.

### Backend

* Java 21.
* Spring Boot 4.1.0.
* Maven Wrapper.
* JUnit.
* MockMvc.

### Control de versiones

* Git.
* GitHub.

## API REST

El backend se encuentra en la carpeta `back-end/` y se ejecuta localmente en el puerto `8080`.

Endpoints disponibles:

| Método | Ruta                 | Descripción                                  |
| ------ | -------------------- | -------------------------------------------- |
| `GET`  | `/api/health`        | Comprueba que el backend está activo.        |
| `GET`  | `/api/memories`      | Devuelve todos los recuerdos disponibles.    |
| `GET`  | `/api/memories/{id}` | Devuelve un recuerdo según su identificador. |

Cuando no existe un recuerdo con el ID solicitado, la API devuelve:

```text
404 Not Found
```

Los datos de los recuerdos se encuentran temporalmente definidos en memoria dentro del backend.

Todavía no existe persistencia en una base de datos.

## Ejecutar el backend

Desde la carpeta `back-end/`:

```bash
./mvnw spring-boot:run
```

En Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

La aplicación estará disponible en:

```text
http://localhost:8080
```

## Ejecutar las pruebas

Desde `back-end/`:

```bash
./mvnw test
```

En Windows:

```powershell
.\mvnw.cmd test
```

La compilación y las pruebas deben finalizar con:

```text
BUILD SUCCESS
```

## Estructura

* `frontend/`: interfaz y archivos de la aplicación.
* `back-end/`: aplicación Java con Spring Boot y API REST.
* `docs/`: documentación técnica y decisiones del proyecto.
* `frontend/assets/`: copias de fotos, videos y miniaturas utilizadas durante el desarrollo.

## Limitaciones actuales

Todavía no se han incorporado:

* PostgreSQL.
* JPA.
* Persistencia de recuerdos.
* Autenticación o autorización.
* Almacenamiento privado de archivos.
* CORS.
* Integración entre frontend y backend.
* Funcionalidades para subir, editar o eliminar recuerdos.

## Reglas del proyecto

* Trabajar paso a paso.
* Probar cada cambio antes de continuar.
* Documentar las decisiones importantes.
* No guardar los únicos archivos originales dentro del proyecto.
* No subir recuerdos familiares privados al repositorio.
* Evitar complejidad innecesaria durante el MVP.
