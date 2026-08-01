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
* Datos obtenidos desde la API REST mediante `GET /api/memories`.
* Backend desarrollado con Java y Spring Boot.
* API REST de solo lectura para consultar recuerdos.
* Endpoint de salud para comprobar el estado del backend.
* Persistencia de metadatos en PostgreSQL.
* Acceso a datos mediante Spring Data JPA.
* Migraciones de base de datos administradas con Flyway.
* Pruebas automatizadas para los endpoints y la persistencia.

La aplicación está enfocada únicamente en la visualización de recuerdos. En esta etapa del MVP, la usuaria no puede subir, editar ni eliminar archivos.

El frontend está conectado con el backend y obtiene los recuerdos mediante la API REST de solo lectura.

Las fotografías, videos y miniaturas continúan almacenados temporalmente en `frontend/assets/`. PostgreSQL almacena únicamente sus metadatos y rutas.

## Tecnologías

### Frontend

* HTML.
* CSS.
* JavaScript nativo.
* Consumo de API REST con `fetch`.

### Backend

* Java 21.
* Spring Boot 4.1.0.
* Spring Data JPA.
* Flyway.
* Maven Wrapper.
* JUnit.
* MockMvc.

### Base de datos

* PostgreSQL.
* Migraciones SQL versionadas con Flyway.

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

El contrato actual de la API mantiene estos campos:

```text
id
title
type
category
date
place
file
thumbnail
description
```

Los metadatos de los recuerdos se almacenan en la tabla `memories` de PostgreSQL.

## Configuración de PostgreSQL

Durante el desarrollo local, el backend obtiene la configuración de conexión mediante variables de entorno:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
```

Ejemplo para Linux:

```bash
export DB_URL='jdbc:postgresql://localhost:5432/recuerdos'
export DB_USERNAME='usuario_local'
export DB_PASSWORD='contraseña_local'
```

Las credenciales reales no deben guardarse en el repositorio.

La base de datos local utilizada durante el desarrollo se llama:

```text
recuerdos
```

## Migraciones

Flyway administra el esquema y los datos iniciales.

Las migraciones se encuentran en:

```text
back-end/src/main/resources/db/migration/
```

Migraciones actuales:

```text
V1__create_memories_table.sql
V2__insert_initial_memories.sql
```

`V1` crea la tabla `memories`.

`V2` inserta tres recuerdos iniciales utilizados para validar la integración entre PostgreSQL, JPA, la API y el frontend.

Las migraciones aplicadas no deben editarse posteriormente. Los cambios futuros en el esquema o en los datos base deben realizarse mediante nuevas migraciones.

## Ejecutar el backend

Antes de iniciar el backend, PostgreSQL debe estar activo y las variables de entorno deben estar definidas.

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

## Ejecutar el frontend

Desde la raíz del proyecto:

```bash
python3 -m http.server 5500 --directory frontend
```

El frontend estará disponible en:

```text
http://localhost:5500
```

El backend debe permanecer activo en `http://localhost:8080` para que el frontend pueda cargar los recuerdos.

## Ejecutar las pruebas

PostgreSQL debe estar activo y las variables de entorno deben estar disponibles en la misma terminal.

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

Las pruebas actuales validan:

* Carga del contexto de Spring Boot.
* Consulta de todos los recuerdos.
* Consulta de un recuerdo por ID.
* Respuesta `404` para IDs inexistentes.
* Configuración CORS para los orígenes locales permitidos.
* Rechazo de orígenes no autorizados.
* Lectura de los tres recuerdos iniciales mediante `MemoryRepository`.

## Estructura

* `frontend/`: interfaz y archivos de la aplicación.
* `frontend/assets/`: fotos, videos y miniaturas utilizadas durante el desarrollo.
* `back-end/`: aplicación Java con Spring Boot y API REST.
* `back-end/src/main/resources/db/migration/`: migraciones SQL de Flyway.
* `docs/`: documentación técnica y decisiones del proyecto.

## Limitaciones actuales

Todavía no se han incorporado:

* Autenticación o autorización.
* Almacenamiento privado de archivos.
* Funcionalidades para subir recuerdos.
* Funcionalidades para editar recuerdos.
* Funcionalidades para eliminar recuerdos.
* Panel administrativo.
* Almacenamiento de archivos en la nube.
* Docker.

PostgreSQL almacena únicamente los metadatos y las rutas. Los archivos físicos continúan temporalmente dentro de `frontend/assets/`.

## Reglas del proyecto

* Trabajar paso a paso.
* Probar cada cambio antes de continuar.
* Documentar las decisiones importantes.
* No guardar los únicos archivos originales dentro del proyecto.
* No subir recuerdos familiares privados al repositorio.
* No guardar credenciales reales en Git.
* Crear nuevas migraciones en lugar de modificar migraciones ya aplicadas.
* Evitar complejidad innecesaria durante el MVP.
