# Alcance del MVP

## Funcionalidades incluidas

### Frontend

* Mostrar una galería de recuerdos.
* Mostrar fotografías.
* Reproducir videos.
* Diferenciar fotografías y videos.
* Navegar por categorías.
* Abrir un recuerdo.
* Regresar fácilmente a la galería.
* Cargar los recuerdos desde `GET /api/memories`.
* Cargar fotografías, videos y miniaturas mediante URLs proporcionadas por la API.
* Mostrar un mensaje accesible cuando la API no está disponible.
* Recuperar la galería cuando el backend vuelve a estar disponible.

### Backend

* Ejecutar una aplicación backend con Java y Spring Boot.
* Comprobar el estado del backend mediante `GET /api/health`.
* Consultar todos los recuerdos mediante `GET /api/memories`.
* Consultar un recuerdo específico mediante `GET /api/memories/{id}`.
* Obtener el archivo principal mediante `GET /api/memories/{id}/file`.
* Obtener la miniatura mediante `GET /api/memories/{id}/thumbnail`.
* Devolver `404 Not Found` cuando el recuerdo o el archivo solicitado no existen.
* Persistir metadatos y storage keys en PostgreSQL.
* Consultar los recuerdos mediante Spring Data JPA.
* Administrar el esquema y los datos iniciales con Flyway.
* Configurar la conexión de PostgreSQL mediante variables de entorno.
* Configurar la raíz del almacenamiento privado mediante `MEDIA_STORAGE_ROOT`.
* Resolver archivos privados mediante `MediaStorageService`.
* Proteger el almacenamiento local contra intentos de path traversal.
* Validar endpoints, persistencia y almacenamiento mediante pruebas automatizadas.
* Permitir solicitudes `GET` desde el frontend local mediante CORS.
* Mantener sin cambios los nombres de los campos del contrato actual de la API.

### Datos y archivos

* Almacenar en PostgreSQL los metadatos y las storage keys de los recuerdos.
* Mantener los archivos físicos fuera de PostgreSQL.
* Mantener fotografías, videos y miniaturas fuera del frontend activo.
* Utilizar almacenamiento privado local para los tres recuerdos actuales.
* Excluir `private-storage/` de Git.
* Trabajar con copias y conservar los archivos originales.
* Cargar tres recuerdos iniciales mediante migraciones Flyway.
* Mantener separadas la entidad de persistencia y la respuesta de la API.
* Mantener separada la lógica de recuerdos de la implementación física del almacenamiento.

## Funcionalidades no incluidas

* Subida de archivos desde la aplicación.
* Edición de recuerdos.
* Eliminación de recuerdos.
* Registro de usuarios.
* Autenticación o autorización.
* Panel administrativo.
* Almacenamiento privado en la nube.
* Importación masiva.
* Extracción automática de metadatos EXIF.
* Eliminación automática de metadatos EXIF.
* Generación automática de miniaturas.
* Paginación.
* Docker.
* Reconocimiento facial.
* Inteligencia artificial.
* Edición de fotografías.
* Comentarios y reacciones.

## Tecnologías actuales

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
* Driver JDBC de PostgreSQL.
* Maven Wrapper.
* JUnit.
* MockMvc.

### Base de datos

* PostgreSQL.
* Migraciones SQL versionadas con Flyway.

### Almacenamiento

* Sistema de archivos local privado.
* Storage keys independientes del proveedor.
* Raíz configurable mediante `MEDIA_STORAGE_ROOT`.
* Acceso a archivos únicamente mediante endpoints del backend.

## Limitaciones actuales

PostgreSQL almacena únicamente metadatos y storage keys. No almacena fotografías, videos ni miniaturas.

La implementación actual del almacenamiento es local y sirve para validar la arquitectura con tres recuerdos.

No se han importado todavía las más de 2.000 fotografías familiares.

La aplicación sigue siendo de solo lectura. La usuaria no puede subir, editar ni eliminar recuerdos desde la interfaz.

La incorporación de nuevos recuerdos continúa siendo una responsabilidad manual del desarrollador hasta que exista una herramienta administrativa futura.

El almacenamiento privado en la nube todavía no forma parte del MVP actual.
