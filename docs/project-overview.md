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

El proyecto cuenta con una base inicial de backend ubicada en la carpeta `back-end/`.

Tecnologías configuradas:

* Java 21.
* Spring Boot 4.1.0.
* Maven mediante Maven Wrapper.
* Servidor web Apache Tomcat integrado.

El backend compila, supera sus pruebas iniciales y puede ejecutarse localmente en el puerto `8080`.

Por ahora, el backend no contiene endpoints propios ni está conectado con el frontend.

### Fuera del alcance actual

Todavía no se han incorporado:

* PostgreSQL.
* Persistencia de datos.
* Autenticación o autorización.
* Almacenamiento de fotografías y videos.
* Funcionalidades para subir, editar o eliminar archivos.
* Integración entre frontend y backend.
