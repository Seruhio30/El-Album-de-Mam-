# Alcance del MVP

## Funcionalidades incluidas

### Frontend

- Mostrar una galería de recuerdos.
- Mostrar fotografías.
- Reproducir videos.
- Diferenciar fotografías y videos.
- Navegar por categorías.
- Abrir un recuerdo.
- Regresar fácilmente a la galería.
- Cargar los recuerdos desde `GET /api/memories`.

### Backend

- Ejecutar una aplicación backend con Java y Spring Boot.
- Comprobar el estado del backend mediante `GET /api/health`.
- Consultar todos los recuerdos mediante `GET /api/memories`.
- Consultar un recuerdo específico mediante `GET /api/memories/{id}`.
- Devolver `404 Not Found` cuando el recuerdo solicitado no existe.
- Mantener temporalmente los recuerdos definidos en memoria.
- Validar los endpoints mediante pruebas automatizadas.
- Permitir solicitudes `GET` desde el frontend local mediante CORS.

## Funcionalidades no incluidas

- Subida de archivos desde la aplicación.
- Edición o eliminación de recuerdos.
- Registro de usuarios.
- Autenticación o autorización.
- PostgreSQL.
- JPA.
- Persistencia de datos.
- Almacenamiento privado de archivos.
- Almacenamiento en la nube.
- Integración entre frontend y backend.
- CORS.
- Reconocimiento facial.
- Inteligencia artificial.
- Edición de fotografías.
- Comentarios y reacciones.
- Integración entre frontend y backend.
- CORS.

## Tecnologías actuales

### Frontend

- HTML.
- CSS.
- JavaScript nativo.
- Consumo de API REST con `fetch`.

### Backend

- Java 21.
- Spring Boot 4.1.0.
- Maven Wrapper.
- JUnit.
- MockMvc.

## Limitación temporal

Los recuerdos todavía se encuentran definidos en memoria dentro del backend.

El frontend consume la API REST, pero todavía no existe persistencia con PostgreSQL ni almacenamiento privado de archivos.