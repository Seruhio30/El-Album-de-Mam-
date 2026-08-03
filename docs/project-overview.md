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

El desarrollador será quien prepare, organice y agregue las fotografías, videos y metadatos de los recuerdos.

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
* Carga de fotografías, videos y miniaturas mediante URLs controladas por la API.

El frontend no accede directamente a la ubicación física ni a las storage keys de los archivos.

Los campos `file` y `thumbnail` recibidos desde la API contienen URLs como:

```text
http://localhost:8080/api/memories/{id}/file
http://localhost:8080/api/memories/{id}/thumbnail
```

El frontend se ejecuta localmente en el puerto `5500` y requiere que el backend esté activo para cargar los recuerdos y archivos multimedia.

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
* `GET /api/memories/{id}/file`: devuelve el archivo principal de un recuerdo.
* `GET /api/memories/{id}/thumbnail`: devuelve la miniatura de un recuerdo.

Cuando el recuerdo o el archivo solicitado no existen, la API devuelve una respuesta `404 Not Found`.

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

`MemoryService` consulta `MemoryRepository` y convierte las entidades en respuestas públicas.

Los valores internos de `file_path` y `thumbnail` no se exponen directamente al frontend. `MemoryService` construye URLs de la API para los campos públicos `file` y `thumbnail`.

Hibernate está configurado con:

```properties
spring.jpa.hibernate.ddl-auto=validate
```

Esta configuración permite validar el esquema, pero evita que Hibernate cree o modifique tablas automáticamente.

Flyway es la herramienta responsable de administrar el esquema y los datos iniciales.

Migraciones actuales:

* `V1__create_memories_table.sql`: crea la tabla `memories`.
* `V2__insert_initial_memories.sql`: inserta tres recuerdos iniciales para validar la integración.
* `V3__replace_media_paths_with_storage_keys.sql`: sustituye las rutas públicas del frontend por storage keys internas.

Los tres registros iniciales son datos controlados para desarrollo y pruebas.

No se han importado todavía las más de 2.000 fotografías familiares.

### Archivos multimedia

PostgreSQL no almacena fotografías, videos ni miniaturas.

La tabla `memories` guarda únicamente:

* Identificador.
* Título.
* Tipo de recuerdo.
* Categoría.
* Fecha.
* Lugar.
* Storage key del archivo principal.
* Storage key de la miniatura.
* Descripción.

Las storage keys siguen una estructura independiente del proveedor:

```text
memories/{id}/{tipo}/{nombre-del-archivo}
```

Ejemplos:

```text
memories/1/photo/viaje-familiar.png
memories/2/video/cumpleanos-familiar.mp4
memories/2/thumbnail/cumpleanos-familiar.jpg
memories/3/photo/tarde-en-familia.jpg
```

Los archivos físicos se almacenan en una carpeta privada cuya raíz se configura mediante:

```text
MEDIA_STORAGE_ROOT
```

Spring Boot recibe esa variable mediante:

```properties
app.storage.root=${MEDIA_STORAGE_ROOT}
```

Durante el desarrollo local se utiliza una carpeta llamada:

```text
private-storage
```

La carpeta está excluida de Git.

Los archivos originales no deben borrarse después de copiarlos y debe mantenerse al menos un respaldo independiente.

### Arquitectura de almacenamiento

El backend define la interfaz:

```text
MediaStorageService
```

La implementación actual es:

```text
LocalMediaStorageService
```

Esta implementación resuelve storage keys dentro de la carpeta privada configurada.

Antes de cargar un archivo, normaliza la ruta y comprueba que permanezca dentro de la raíz privada. Esta validación evita intentos de path traversal.

`MemoryMediaService` conecta los recuerdos almacenados en PostgreSQL con el servicio de almacenamiento:

```text
ID del recuerdo
→ MemoryRepository
→ storage key
→ MediaStorageService
→ archivo privado
```

La implementación local podrá sustituirse en el futuro por almacenamiento privado en la nube sin cambiar el contrato de la API ni las storage keys guardadas en PostgreSQL.

### Integración actual

El frontend y el backend están conectados.

El módulo `frontend/js/data/memories-service.js` consulta:

```text
GET http://localhost:8080/api/memories
```

mediante `fetch`.

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

Los campos `file` y `thumbnail` contienen URLs de la API, no rutas físicas ni storage keys.

Los filtros, el visor de fotografías y el reproductor de videos continúan funcionando sin cambios en su lógica principal.

El flujo actual es:

```text
PostgreSQL
→ MemoryRepository
→ MemoryService
→ MemoryResponse
→ API REST
→ frontend
```

Para los archivos multimedia:

```text
PostgreSQL
→ MemoryRepository
→ MemoryMediaService
→ MediaStorageService
→ almacenamiento privado
→ endpoint multimedia
→ frontend
```

### Pruebas

La suite actual ejecuta 19 pruebas automatizadas.

Las pruebas validan:

* Carga del contexto de Spring Boot.
* Consulta de todos los recuerdos.
* Consulta de un recuerdo por identificador.
* Respuesta `404 Not Found`.
* Configuración CORS.
* Rechazo de orígenes no autorizados.
* Lectura de los tres recuerdos iniciales mediante `MemoryRepository`.
* Carga de archivos desde el almacenamiento privado local.
* Rechazo de storage keys fuera de la raíz privada.
* Respuesta ante archivos inexistentes.
* Resolución de archivos y miniaturas mediante `MemoryMediaService`.
* Entrega de fotografías, miniaturas y videos mediante la API.
* Tipos de contenido correctos para PNG, JPEG y MP4.
* URLs públicas correctas en `file` y `thumbnail`.

La suite completa termina con:

```text
Tests run: 19, Failures: 0, Errors: 0
BUILD SUCCESS
```

Las pruebas que cargan el contexto completo requieren que PostgreSQL esté activo y que las variables de entorno estén definidas.

La integración también fue validada manualmente comprobando:

* Carga de los tres recuerdos.
* Carga de las tres miniaturas.
* Apertura de ambas fotografías.
* Reproducción del video.
* Ausencia de errores relevantes de CORS o carga multimedia.

### Fuera del alcance actual

Todavía no se han incorporado:

* Autenticación o autorización.
* Almacenamiento privado en la nube.
* Funcionalidades para subir archivos.
* Funcionalidades para editar recuerdos.
* Funcionalidades para eliminar recuerdos.
* Panel administrativo.
* Importación masiva.
* Extracción o eliminación automática de metadatos EXIF.
* Generación automática de miniaturas.
* Paginación.
* Docker.
