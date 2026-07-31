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
- Cargar temporalmente los recuerdos desde `frontend/data/memories.json`.

### Backend

- Ejecutar una aplicación backend con Java y Spring Boot.
- Comprobar el estado del backend mediante `GET /api/health`.
- Consultar todos los recuerdos mediante `GET /api/memories`.
- Consultar un recuerdo específico mediante `GET /api/memories/{id}`.
- Devolver `404 Not Found` cuando el recuerdo solicitado no existe.
- Mantener temporalmente los recuerdos definidos en memoria.
- Validar los endpoints mediante pruebas automatizadas.

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

## Tecnologías actuales

### Frontend

- HTML.
- CSS.
- JavaScript nativo.
- JSON local.

### Backend

- Java 21.
- Spring Boot 4.1.0.
- Maven Wrapper.
- JUnit.
- MockMvc.

## Limitación temporal

Aunque la API REST ya existe, el frontend todavía continúa cargando los recuerdos desde `frontend/data/memories.json`.

La integración entre ambos componentes se realizará en una etapa futura.