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
* Mostrar un mensaje accesible cuando la API no está disponible.
* Recuperar la galería cuando el backend vuelve a estar disponible.

### Backend

* Ejecutar una aplicación backend con Java y Spring Boot.
* Comprobar el estado del backend mediante `GET /api/health`.
* Consultar todos los recuerdos mediante `GET /api/memories`.
* Consultar un recuerdo específico mediante `GET /api/memories/{id}`.
* Devolver `404 Not Found` cuando el recuerdo solicitado no existe.
* Persistir metadatos de recuerdos en PostgreSQL.
* Consultar los recuerdos mediante Spring Data JPA.
* Administrar el esquema y los datos iniciales con Flyway.
* Configurar la conexión mediante variables de entorno.
* Validar los endpoints y la persistencia mediante pruebas automatizadas.
* Permitir solicitudes `GET` desde el frontend local mediante CORS.
* Mantener sin cambios el contrato actual de la API.

### Datos y archivos

* Almacenar en PostgreSQL los metadatos y rutas de los recuerdos.
* Mantener temporalmente fotografías, videos y miniaturas en `frontend/assets`.
* Cargar tres recuerdos iniciales mediante una migración Flyway.
* Mantener separadas la entidad de persistencia y la respuesta de la API.

## Funcionalidades no incluidas

* Subida de archivos desde la aplicación.
* Edición de recuerdos.
* Eliminación de recuerdos.
* Registro de usuarios.
* Autenticación o autorización.
* Panel administrativo.
* Almacenamiento privado de archivos.
* Almacenamiento en la nube.
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

## Limitación temporal

PostgreSQL almacena únicamente los metadatos y las rutas de los recuerdos.

Las fotografías, videos y miniaturas continúan temporalmente dentro de `frontend/assets`.

La aplicación sigue siendo de solo lectura. La usuaria no puede subir, editar ni eliminar recuerdos desde la interfaz.

La incorporación de nuevos recuerdos continúa siendo una responsabilidad del desarrollador hasta que exista una herramienta administrativa futura.
