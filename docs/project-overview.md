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

* Interfaz sencilla.
* Botones grandes.
* Textos claros.
* Navegación consistente.
* Pocas acciones por pantalla.
* Seguridad y protección de los archivos.

## Estado técnico actual

### Frontend

El frontend está desarrollado con HTML, CSS y JavaScript nativo.

Actualmente incluye:

* Galería dinámica de fotografías y videos.
* Filtros por categorías.
* Visor interno para fotografías.
* Reproductor interno para videos.
* Diseño responsive y accesible.
* Consumo de recuerdos desde `GET /api/memories`.
* Archivos multimedia de desarrollo almacenados en `frontend/assets`.

El frontend se ejecuta localmente en el puerto `5500` y requiere que el backend esté activo para cargar los recuerdos.

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

El backend permite solicitudes `GET` desde el frontend local mediante una configuración CORS limitada a:

* `http://localhost:5500`.
* `http://127.0.0.1:5500`.

El backend todavía no utiliza una base de datos.

### Integración actual

El frontend y el backend están conectados.

El módulo `frontend/js/data/memories-service.js` consulta `GET http://localhost:8080/api/memories` mediante `fetch`.

La respuesta de la API mantiene el contrato que necesita el frontend, por lo que los filtros, el visor de fotografías y el reproductor de videos continúan funcionando sin adaptaciones adicionales.

Las rutas de fotografías, videos y miniaturas continúan apuntando a archivos ubicados en `frontend/assets`.

### Fuera del alcance actual

Todavía no se han incorporado:

* PostgreSQL.
* JPA.
* Persistencia de datos.
* Autenticación o autorización.
* Almacenamiento privado de fotografías y videos.
* Almacenamiento en la nube.
* Funcionalidades para subir, editar o eliminar archivos.
