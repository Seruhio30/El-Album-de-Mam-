# Descripción general del proyecto

## Nombre

El Álbum de Mamá

## Problema

La usuaria principal tiene muchas fotografías y videos almacenados en su teléfono y se confunde al buscarlos manualmente.

## Solución

Crear una aplicación web sencilla que permita visualizar recuerdos familiares organizados en una galería accesible.

## Usuario principal

Adulta mayor con poca experiencia tecnológica.

## Responsabilidad del desarrollador

El desarrollador será quien prepare, organice y agregue las fotografías y videos.

La usuaria no podrá subir ni administrar archivos durante el MVP inicial.

## Principios

- Interfaz sencilla.
- Botones grandes.
- Textos claros.
- Navegación consistente.
- Pocas acciones por pantalla.
- Seguridad y protección de los archivos.

## Estado técnico actual

### Frontend

El frontend está desarrollado con HTML, CSS y JavaScript nativo.

Actualmente incluye:

* Galería dinámica de fotografías y videos.
* Filtros por categorías.
* Visor interno para fotografías.
* Reproductor interno para videos.
* Diseño responsive y accesible.
* Datos cargados desde `frontend/data/memories.json`.

### Backend

El proyecto cuenta con un backend ubicado en la carpeta `back-end/`.

Tecnologías configuradas:

* Java 21.
* Spring Boot 4.1.0.
* Maven mediante Maven Wrapper.
* Servidor web Apache Tomcat integrado.
* JUnit y MockMvc para pruebas automatizadas.

El backend compila, supera sus pruebas automatizadas y puede ejecutarse localmente en el puerto `8080`.

Actualmente incluye una API REST de solo lectura.

Endpoints disponibles:

* `GET /api/health`: comprueba que la aplicación está activa.
* `GET /api/memories`: devuelve todos los recuerdos disponibles.
* `GET /api/memories/{id}`: devuelve un recuerdo según su identificador.

Cuando el recuerdo solicitado no existe, el endpoint individual devuelve una respuesta `404 Not Found`.

Los recuerdos se encuentran definidos temporalmente en memoria dentro de `MemoryService`.

El backend todavía no utiliza una base de datos y no está conectado con el frontend.

### Fuera del alcance actual

Todavía no se han incorporado:

* PostgreSQL.
* JPA.
* Persistencia de datos.
* Autenticación o autorización.
* Almacenamiento de fotografías y videos.
* CORS.
* Funcionalidades para subir, editar o eliminar archivos.
* Integración entre frontend y backend.

### Fuera del alcance actual

Todavía no se han incorporado:

* PostgreSQL.
* Persistencia de datos.
* Autenticación o autorización.
* Almacenamiento de fotografías y videos.
* Funcionalidades para subir, editar o eliminar archivos.
* Integración entre frontend y backend.
