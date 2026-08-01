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

El desarrollador será quien prepare, organizar y agregar las fotografías, videos y metadatos de los recuerdos.

La usuaria no podrá subir ni administrar archivos durante el MVP inicial.

## Principios

* Interfaz sencilla.
* Botones grandes.
* Textos claros.
* Navegación consistente.
* Pocas acciones por pantalla.
* Seguridad y protección de los archivos.
* Separación entre archivos multimedia y metadatos.
* Evitar complejidad innecesaria durante el MVP.

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
* Spring Data JPA.
* Flyway.
* Driver JDBC de PostgreSQL.
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

El backend permite solicitudes `GET` desde el frontend local mediante una configuración CORS limitada a:

* `http://localhost:5500`.
* `http://127.0.0.1:5500`.

### Persistencia

Los metadatos de los recuerdos se almacenan en PostgreSQL.

La conexión se configura mediante las variables de entorno:

* `DB_URL`.
* `DB_USERNAME`.
* `DB_PASSWORD`.

Las credenciales reales no se almacenan en el repositorio.

Spring Data JPA se utiliza para consultar la tabla `memories`.

La entidad `Memory` representa los datos persistidos y `MemoryResponse` mantiene el contrato de salida de la API.

`MemoryService` consulta `MemoryRepository` y convierte las entidades en respuestas HTTP.

Hibernate está configurado con:

```properties
spring.jpa.hibernate.ddl-auto=validate
```

Esta configuración permite validar el esquema, pero evita que Hibernate cree o modifique tablas automáticamente.

Flyway es la herramienta responsable de administrar el esquema y los datos iniciales.

Migraciones actuales:

* `V1__create_memories_table.sql`: crea la tabla `memories`.
* `V2__insert_initial_memories.sql`: inserta tres recuerdos iniciales para validar la integración.

Los tres registros iniciales son datos controlados para desarrollo y pruebas. Los recuerdos adicionales deben incorporarse mediante nuevas migraciones o mediante una herramienta administrativa futura.

### Archivos multimedia

PostgreSQL almacena únicamente los metadatos y las rutas de los archivos.

Las fotografías, videos y miniaturas continúan temporalmente en:

```text
frontend/assets
```

La tabla `memories` almacena información como:

* Identificador.
* Título.
* Tipo de recuerdo.
* Categoría.
* Fecha.
* Lugar.
* Ruta del archivo.
* Ruta de la miniatura.
* Descripción.

El almacenamiento privado de archivos se implementará en una etapa futura.

### Integración actual

El frontend y el backend están conectados.

El módulo `frontend/js/data/memories-service.js` consulta `GET http://localhost:8080/api/memories` mediante `fetch`.

La respuesta de la API mantiene el contrato que necesita el frontend:

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

Los filtros, el visor de fotografías y el reproductor de videos continúan funcionando sin adaptaciones adicionales.

Las rutas de fotografías, videos y miniaturas continúan apuntando a archivos ubicados en `frontend/assets`.

### Pruebas

Las pruebas automatizadas validan:

* Carga del contexto de Spring Boot.
* Consulta de todos los recuerdos.
* Consulta de un recuerdo por identificador.
* Respuesta `404 Not Found`.
* Configuración CORS.
* Rechazo de orígenes no autorizados.
* Lectura de los tres recuerdos iniciales mediante `MemoryRepository`.

Las pruebas requieren que PostgreSQL esté activo y que las variables de entorno estén definidas.

### Fuera del alcance actual

Todavía no se han incorporado:

* Autenticación o autorización.
* Almacenamiento privado de fotografías y videos.
* Almacenamiento en la nube.
* Funcionalidades para subir archivos.
* Funcionalidades para editar recuerdos.
* Funcionalidades para eliminar recuerdos.
* Panel administrativo.
* Docker.
