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
* Almacenamiento privado local para fotografías, videos y miniaturas.
* Acceso a los archivos multimedia exclusivamente mediante el backend.
* Pruebas automatizadas para endpoints, persistencia y almacenamiento.

La aplicación está enfocada únicamente en la visualización de recuerdos. En esta etapa del MVP, la usuaria no puede subir, editar ni eliminar archivos.

El frontend obtiene los recuerdos mediante la API REST de solo lectura.

PostgreSQL almacena los metadatos y las storage keys internas. Los archivos multimedia se guardan fuera del frontend, dentro de una ubicación privada configurable.

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

| Método | Ruta                           | Descripción                                   |
| ------ | ------------------------------ | --------------------------------------------- |
| `GET`  | `/api/health`                  | Comprueba que el backend está activo.         |
| `GET`  | `/api/memories`                | Devuelve una página de recuerdos.             |
| `GET`  | `/api/memories/{id}`           | Devuelve un recuerdo según su identificador.  |
| `GET`  | `/api/memories/{id}/file`      | Devuelve el archivo principal de un recuerdo. |
| `GET`  | `/api/memories/{id}/thumbnail` | Devuelve la miniatura asociada a un recuerdo. |

### Paginación y filtrado

El endpoint `GET /api/memories` acepta estos parámetros opcionales:

| Parámetro  | Descripción                                      | Valor predeterminado |
| ---------- | ------------------------------------------------ | -------------------- |
| `page`     | Número de página, comenzando en `0`.             | `0`                  |
| `size`     | Cantidad máxima de recuerdos por página.         | `6`                  |
| `category` | Categoría por la que se filtrarán los recuerdos. | Sin filtro           |

El tamaño máximo permitido es `24`.

Ejemplos:

```text
GET /api/memories?page=0&size=6
GET /api/memories?page=0&size=6&category=viajes
```

Los recuerdos se ordenan de forma estable mediante:

```text
memory_date DESC
id DESC
```

La respuesta paginada contiene estos campos:

```text
content
page
size
totalElements
totalPages
hasNext
```

Ejemplo:

```json
{
  "content": [
    {
      "id": 3,
      "title": "Tarde en familia",
      "type": "photo",
      "category": "familia",
      "date": "2025-12-20",
      "place": "San José",
      "file": "http://localhost:8080/api/memories/3/file",
      "thumbnail": "http://localhost:8080/api/memories/3/thumbnail",
      "description": "Una tarde tranquila compartiendo juntos."
    }
  ],
  "page": 0,
  "size": 6,
  "totalElements": 3,
  "totalPages": 1,
  "hasNext": false
}
```

Cada elemento de `content` conserva los campos actuales de `MemoryResponse`:

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

Los campos `file` y `thumbnail` contienen URLs de la API. El frontend no recibe la ubicación física ni las storage keys internas de los archivos.

La respuesta paginada utiliza el DTO propio `PagedMemoryResponse`. La API no expone directamente tipos internos de Spring como `Page` o `Pageable`.

Cuando no existe el recuerdo o el archivo solicitado, la API devuelve:

```text
404 Not Found
```

Los metadatos de los recuerdos se almacenan en la tabla `memories` de PostgreSQL.

---

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

## Configuración del almacenamiento privado

La raíz del almacenamiento multimedia se configura mediante:

```text
MEDIA_STORAGE_ROOT
```

El valor debe ser una ruta absoluta hacia una carpeta privada del sistema.

Ejemplo para Linux:

```bash
export MEDIA_STORAGE_ROOT='/ruta/absoluta/al/private-storage'
```

Spring Boot recibe esta configuración mediante:

```properties
app.storage.root=${MEDIA_STORAGE_ROOT}
```

La carpeta externa utilizada para validar futuras importaciones se configura de forma independiente mediante:

```text
IMPORT_ROOT
```

Ejemplo para Linux:

```bash
export IMPORT_ROOT='/ruta/absoluta/a/recuerdos-import'
```

Spring Boot recibe esta configuración mediante:

```properties
app.import.root=${IMPORT_ROOT}
```

`IMPORT_ROOT` se utiliza únicamente como origen de manifiestos y archivos candidatos durante la validación `dry-run`.

La carpeta de importación debe permanecer separada de `private-storage`.

La carpeta `private-storage/` está excluida de Git.

Los archivos utilizados para validar los tres recuerdos actuales siguen esta estructura:

```text
private-storage/
└── memories/
    ├── 1/
    │   └── photo/
    ├── 2/
    │   ├── video/
    │   └── thumbnail/
    └── 3/
        └── photo/
```

PostgreSQL no almacena los archivos físicos. Las columnas `file_path` y `thumbnail` contienen storage keys como:

```text
memories/1/photo/viaje-familiar.png
memories/2/video/cumpleanos-familiar.mp4
memories/2/thumbnail/cumpleanos-familiar.jpg
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
V3__replace_media_paths_with_storage_keys.sql
```

`V1` crea la tabla `memories`.

`V2` inserta los tres recuerdos iniciales utilizados para validar la integración entre PostgreSQL, JPA, la API y el frontend.

`V3` reemplaza las rutas públicas de `frontend/assets` por storage keys internas independientes del proveedor de almacenamiento.

Las migraciones aplicadas no deben editarse posteriormente. Los cambios futuros en el esquema o en los datos base deben realizarse mediante nuevas migraciones.

## Ejecutar el backend

Antes de iniciar el backend:

* PostgreSQL debe estar activo.
* Las variables `DB_URL`, `DB_USERNAME` y `DB_PASSWORD` deben estar definidas.
* `MEDIA_STORAGE_ROOT` debe apuntar a una ruta absoluta válida.

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

Si la terminal está ubicada dentro de `back-end/`, puede utilizarse:

```bash
python3 -m http.server 5500 --directory ../frontend
```

El frontend estará disponible en:

```text
http://localhost:5500
```

El backend debe permanecer activo en `http://localhost:8080` para que el frontend pueda cargar los recuerdos y archivos multimedia.

## Ejecutar las pruebas

PostgreSQL debe estar activo y las variables de entorno deben estar disponibles en la misma terminal.

Desde `back-end/`:

```bash
./mvnw test
```

La compilación y las pruebas deben finalizar con:

```text
BUILD SUCCESS
```

La suite actual ejecuta 65 pruebas y valida:

* Carga del contexto de Spring Boot.
* Consulta paginada de recuerdos.
* Orden estable por fecha e identificador.
* Filtrado por categoría sin distinguir mayúsculas y minúsculas.
* Valores predeterminados de paginación.
* Límite máximo del tamaño de página.
* Metadatos de la respuesta paginada.
* Consulta de un recuerdo por ID.
* Respuesta `404` para IDs inexistentes.
* Configuración CORS para los orígenes locales permitidos.
* Rechazo de orígenes no autorizados.
* Lectura de los tres recuerdos iniciales mediante `MemoryRepository`.
* Carga de archivos desde el almacenamiento privado local.
* Rechazo de storage keys que intentan salir de la raíz privada.
* Respuesta ante archivos inexistentes.
* Resolución de archivos y miniaturas mediante `MemoryMediaService`.
* Entrega de fotografías, videos y miniaturas mediante la API.
* Tipos de contenido correctos para PNG, JPEG y MP4.
* URLs públicas de la API en los campos `file` y `thumbnail`.
* Configuración independiente de `IMPORT_ROOT`.
* Lectura segura de manifiestos CSV en UTF-8.
* Validación exacta de encabezados CSV.
* Validación de campos obligatorios y límites de longitud.
* Validación de identificadores positivos y fechas ISO válidas.
* Validación de tipos y categorías permitidos.
* Resolución segura de archivos candidatos dentro de `IMPORT_ROOT`.
* Rechazo de rutas absolutas, path traversal y archivos inexistentes.
* Validación de extensiones para fotografías, videos y miniaturas.
* Detección de identificadores, filas y rutas repetidas dentro del manifiesto.
* Generación de un reporte consolidado de validación `dry-run`.
* Ejecución del flujo sin copiar archivos ni modificar PostgreSQL o `private-storage`.

La suite completa termina con:

```text
Tests run: 65, Failures: 0, Errors: 0
BUILD SUCCESS
```

## Estructura

* `frontend/`: interfaz web de la aplicación.
* `frontend/assets/`: archivos temporales o copias de desarrollo; ya no es la fuente activa utilizada por la API.
* `back-end/`: aplicación Java con Spring Boot y API REST.
* `back-end/src/main/java/com/album_de_mama/back_end/storage/`: configuración y servicios de almacenamiento.
* `back-end/src/main/java/com/album_de_mama/back_end/importvalidation/`: configuración, modelos y servicios de validación `dry-run`.
* `back-end/src/main/resources/db/migration/`: migraciones SQL de Flyway.
* `private-storage/`: archivos multimedia privados locales, excluidos de Git.
* `docs/`: documentación técnica y decisiones del proyecto.

## Limitaciones actuales

Todavía no se han incorporado:

* Autenticación o autorización.
* Funcionalidades para subir recuerdos.
* Funcionalidades para editar recuerdos.
* Funcionalidades para eliminar recuerdos.
* Panel administrativo.
* Importación real de archivos y metadatos.
* Extracción o eliminación automática de metadatos EXIF.
* Generación automática de miniaturas.
* Almacenamiento privado en la nube.
* Docker.

La implementación actual utiliza almacenamiento privado local para validar la arquitectura con tres recuerdos.

Existe una validación previa `dry-run` para manifiestos CSV y archivos candidatos, pero todavía no copia archivos, genera storage keys ni inserta recuerdos en PostgreSQL.

No se han importado las más de 2.000 fotografías familiares.

## Reglas del proyecto

* Trabajar paso a paso.
* Probar cada cambio antes de continuar.
* Documentar las decisiones importantes.
* No guardar los únicos archivos originales dentro del proyecto.
* No borrar los archivos originales después de copiarlos.
* No subir recuerdos familiares privados al repositorio.
* Mantener al menos un respaldo independiente de los archivos originales.
* No guardar credenciales reales en Git.
* Crear nuevas migraciones en lugar de modificar migraciones ya aplicadas.
* Preferir storage keys internas sobre URLs dependientes de un proveedor.
* Evitar complejidad innecesaria durante el MVP.
