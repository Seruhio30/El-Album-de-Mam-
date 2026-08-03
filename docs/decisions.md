# Decisiones técnicas

## 1. Frontend sin framework

Se utilizarán HTML, CSS y JavaScript sin React o Vue durante la primera etapa.

### Motivo

El MVP es pequeño y no requiere todavía la complejidad de un framework.

## 2. Datos locales

Los recuerdos se definirán inicialmente en un archivo JSON.

### Motivo

Esto permite validar la interfaz antes de construir un backend y una base de datos.

## 3. Archivos multimedia fuera de Git

Las fotografías y videos estarán ignorados mediante `.gitignore`.

### Motivo

Los recuerdos son privados y pueden ocupar demasiado espacio.

## 4. Estructura semántica de la pantalla principal

La pantalla principal utiliza elementos HTML semánticos como `header`, `main`,
`section`, `nav`, `article` y `footer`.

### Motivo

Una estructura semántica mejora la accesibilidad, facilita el mantenimiento y
permite que tecnologías de asistencia comprendan mejor el contenido.

## 5. Diseño accesible y adaptable

La interfaz utiliza texto de tamaño amplio, tarjetas con áreas táctiles grandes,
contraste visible y estados de enfoque para navegación mediante teclado.

El diseño comienza con una columna para teléfonos y aumenta progresivamente a
dos o cuatro columnas según el espacio disponible.

### Motivo

La usuaria principal tiene poca experiencia tecnológica. La interfaz debe ser
fácil de leer, tocar y comprender desde un teléfono o una tableta.

## 6. Generación dinámica de recuerdos

Las tarjetas de recuerdos se generan con JavaScript a partir del archivo
`frontend/data/memories.json`.

### Motivo

Separar los datos del HTML permite agregar, editar o eliminar recuerdos sin
modificar directamente la estructura de la página.

También prepara el frontend para consumir una API REST en una etapa futura.

## 7. Uso de copias multimedia durante el desarrollo

Durante el desarrollo se utilizan copias de fotografías y videos dentro de
`frontend/assets`.

Estos archivos están excluidos del repositorio mediante `.gitignore`.

### Motivo

Los recuerdos familiares son privados y pueden tener un tamaño considerable.
Los archivos originales deben mantenerse fuera del proyecto y contar con al
menos un respaldo independiente.

## 8. Visor interno de fotografías

Las fotografías se abren dentro de la misma aplicación mediante un visor
dedicado.

El visor muestra la imagen, el título, la fecha, el lugar y la descripción del
recuerdo. También incluye un botón visible para regresar a la galería.

### Motivo

Abrir la fotografía directamente como archivo puede desorientar a la usuaria.
Mantener la navegación dentro de la aplicación ofrece una experiencia más
predecible y sencilla.

## 9. Reproductor interno de videos

Los videos se reproducen dentro de la aplicación mediante el elemento HTML
`video`.

El reproductor incluye controles nativos del navegador, información del
recuerdo y un botón visible para regresar a la galería.

Al cerrar el visor, el video se pausa y se elimina temporalmente su fuente.

### Motivo

Mantener la reproducción dentro de la aplicación evita que la usuaria salga del
flujo principal o se desoriente al abrir el archivo directamente.

## 10. Filtros de categorías en el frontend

Las categorías de la pantalla principal filtran los recuerdos cargados desde
`memories.json`.

Los datos originales se mantienen en memoria y la galería se vuelve a renderizar
según la categoría seleccionada.

La categoría activa se comunica visualmente y mediante el atributo
`aria-current`.

### Motivo

El filtrado permite encontrar recuerdos con menos pasos y sin presentar
formularios o controles complejos a la usuaria.

## 11. Uso de módulos JavaScript nativos

El código JavaScript comenzó a dividirse en módulos según su responsabilidad.

En esta primera refactorización se separaron:

- El formateo de fechas en `js/utils/date.js`.
- La carga y validación de recuerdos en `js/data/memories-service.js`.

El archivo `app.js` continúa coordinando la aplicación.

### Motivo

Los módulos reducen el tamaño y las responsabilidades de `app.js`, facilitan
las pruebas y permiten continuar agregando funcionalidades sin crear un archivo
central difícil de mantener.

Se utilizan módulos nativos del navegador para evitar dependencias y
herramientas de compilación innecesarias durante el MVP.

## 12. Componente modular para tarjetas de recuerdos

La creación y renderización de las tarjetas se trasladó al módulo
`js/components/memory-card.js`.

El componente recibe funciones para abrir fotografías y videos, por lo que no
controla directamente los visores.

### Motivo

Separar la presentación de las tarjetas reduce las responsabilidades de
`app.js` y evita acoplar el componente con la navegación de la aplicación.

## 13. Módulo independiente para el visor de fotografías

La lógica para abrir y cerrar fotografías se trasladó a
`js/viewers/photo-viewer.js`.

El módulo recibe los elementos y funciones que necesita mediante parámetros,
en lugar de buscar directamente toda la interfaz.

### Motivo

Esto reduce el acoplamiento con `app.js`, mantiene la lógica del visor en un
solo lugar y facilita futuras mejoras sin afectar otras partes de la aplicación.

## 14. Módulo independiente para el visor de videos

La lógica para abrir, reproducir y cerrar videos se trasladó a
`js/viewers/video-viewer.js`.

El módulo también pausa el video y elimina temporalmente su fuente al cerrar el
visor.

### Motivo

Separar el reproductor reduce las responsabilidades de `app.js` y mantiene en
un solo lugar el manejo del estado multimedia.

## 15. Módulo independiente para filtros de categorías

La lógica de filtrado se trasladó a
`js/filters/category-filter.js`.

El módulo recibe una función para obtener los recuerdos y otra para renderizar
el resultado filtrado.

### Motivo

Esto separa el estado de los recuerdos, la lógica de filtrado y la presentación
de las tarjetas, dejando `app.js` como coordinador.

---

## 16. API REST de solo lectura para recuerdos

El backend expone una API REST para consultar recuerdos mediante los siguientes endpoints:

- `GET /api/memories`.
- `GET /api/memories/{id}`.

La API permite únicamente operaciones de lectura.

Cuando no existe un recuerdo con el identificador solicitado, el backend devuelve una respuesta `404 Not Found`.

### Motivo

La aplicación está enfocada en que la usuaria visualice recuerdos de forma sencilla.

En esta etapa del MVP no se requieren operaciones para subir, editar o eliminar archivos desde la aplicación.

Limitar la API a consultas reduce la complejidad inicial y permite validar primero la estructura del backend antes de incorporar persistencia, autenticación o administración de archivos.

## 17. Recuerdos temporales definidos en memoria

Los recuerdos del backend se encuentran definidos temporalmente dentro de `MemoryService`.

El servicio mantiene una lista inmutable y ofrece operaciones para:

- Obtener todos los recuerdos.
- Buscar un recuerdo por su identificador.

### Motivo

La persistencia con PostgreSQL y JPA todavía está fuera del alcance actual.

Utilizar datos en memoria permite construir y probar los endpoints REST sin agregar prematuramente configuración de base de datos, entidades, repositorios o migraciones.

Esta implementación es temporal y será reemplazada cuando se incorpore persistencia.
## 18. CORS limitado al frontend local

El backend permite solicitudes de origen cruzado únicamente desde los siguientes orígenes de desarrollo:

- `http://localhost:5500`.
- `http://127.0.0.1:5500`.

La configuración se aplica solamente a las rutas `/api/**` y permite únicamente solicitudes con el método `GET`.

No se habilitan credenciales ni se utiliza un origen abierto mediante `*`.

### Motivo

Durante el desarrollo, el frontend y el backend se ejecutan en puertos diferentes y el navegador considera que pertenecen a orígenes distintos.

Limitar CORS a los orígenes locales conocidos y a operaciones de lectura mantiene la configuración alineada con la API actual y evita habilitar acceso más amplio del necesario.

Esta configuración es exclusiva para desarrollo local y deberá revisarse cuando se defina el despliegue del frontend.

---

## 19. Frontend conectado con la API REST de recuerdos

El frontend obtiene los recuerdos mediante una solicitud `GET` a:

`http://localhost:8080/api/memories`

La lectura anterior desde `frontend/data/memories.json` dejó de ser la fuente activa de datos.

El módulo `frontend/js/data/memories-service.js` mantiene la responsabilidad de obtener y validar la lista de recuerdos.

### Motivo

Mantener el acceso a datos dentro de un módulo independiente permite cambiar la fuente sin modificar `app.js`, los filtros, el visor de fotografías ni el reproductor de videos.

El contrato de la API coincide con la estructura utilizada previamente por el frontend, por lo que no fue necesario adaptar nombres ni formatos.

Durante el desarrollo local, el frontend debe ejecutarse en el puerto `5500` y el backend en el puerto `8080`.

La integración fue validada manualmente comprobando:

- Carga de los tres recuerdos.
- Funcionamiento de los filtros.
- Apertura y cierre de fotografías.
- Reproducción y cierre de videos.
- Ausencia de errores CORS y errores de carga en la consola.

---

## 20. Mensaje accesible cuando la API no está disponible

Cuando el frontend no puede cargar los recuerdos desde la API, muestra un mensaje claro dentro de la galería:

`No pudimos cargar los recuerdos en este momento. Por favor, inténtalo nuevamente más tarde.`

El mensaje utiliza `role="alert"` para que los lectores de pantalla lo anuncien cuando aparece.

### Motivo

La usuaria principal no necesita ver términos técnicos como API, servidor o backend.

El mensaje debe explicar el problema de forma sencilla y mantener la interfaz comprensible, incluso cuando el backend no esté disponible.

El error técnico continúa registrándose en la consola para facilitar la depuración durante el desarrollo.

### Validación

La funcionalidad fue probada manualmente con el backend apagado y encendido nuevamente.

Se confirmó que:

- El mensaje aparece cuando la API no está disponible.
- No se muestran tarjetas antiguas.
- Los recuerdos vuelven a aparecer al restaurar el backend y recargar la página.

---

## 21. PostgreSQL para persistir metadatos de recuerdos

Los metadatos de los recuerdos se almacenan en una base de datos PostgreSQL.

La tabla `memories` contiene:

* Identificador.
* Título.
* Tipo.
* Categoría.
* Fecha.
* Lugar.
* Ruta del archivo.
* Ruta de la miniatura.
* Descripción.

PostgreSQL no almacena actualmente las fotografías, videos ni miniaturas.

### Motivo

La lista temporal definida en `MemoryService` permitía validar la API, pero no ofrecía persistencia real.

Guardar los metadatos en PostgreSQL permite conservar la información, consultarla mediante el backend y preparar el proyecto para incorporar más recuerdos sin mantenerlos escritos directamente en el código Java.

La aplicación continúa siendo de solo lectura y no incorpora todavía operaciones para crear, editar o eliminar recuerdos.

---

## 22. Configuración de PostgreSQL mediante variables de entorno

La conexión del backend con PostgreSQL se configura mediante:

* `DB_URL`.
* `DB_USERNAME`.
* `DB_PASSWORD`.

El archivo `application.properties` contiene únicamente referencias a estas variables y no incluye credenciales reales.

### Motivo

Las credenciales y datos de conexión pueden variar entre entornos y no deben almacenarse en Git.

El uso de variables de entorno permite mantener la configuración sensible fuera del repositorio y facilita utilizar una configuración diferente durante desarrollo, pruebas o despliegue.

---

## 23. Flyway como responsable del esquema de la base de datos

Flyway administra la creación y evolución del esquema de PostgreSQL.

Las migraciones iniciales son:

* `V1__create_memories_table.sql`: crea la tabla `memories`.
* `V2__insert_initial_memories.sql`: inserta los tres recuerdos iniciales.

Hibernate está configurado con:

```properties
spring.jpa.hibernate.ddl-auto=validate
```

### Motivo

El esquema debe evolucionar mediante archivos SQL versionados y reproducibles.

La opción `validate` permite que Hibernate compruebe la correspondencia entre las entidades y la base de datos, pero evita que cree o modifique tablas automáticamente.

Las migraciones ya aplicadas no deben editarse. Los cambios futuros deben realizarse mediante nuevas migraciones.

---

## 24. Spring Data JPA para consultar recuerdos

El backend utiliza Spring Data JPA para acceder a la tabla `memories`.

La interfaz `MemoryRepository` extiende `JpaRepository<Memory, Long>` y proporciona las operaciones de consulta necesarias para:

* Obtener todos los recuerdos.
* Buscar un recuerdo por su identificador.

### Motivo

Los endpoints actuales requieren únicamente consultas sencillas.

Spring Data JPA evita implementar manualmente código JDBC o consultas repetitivas y mantiene el acceso a datos separado del controlador y de la lógica del servicio.

No se agregaron operaciones de escritura porque están fuera del alcance actual del MVP.

---

## 25. Separación entre entidad de persistencia y respuesta de la API

La clase `Memory` representa la entidad almacenada en PostgreSQL.

El record `MemoryResponse` continúa representando la respuesta pública de la API.

`MemoryService` consulta las entidades mediante `MemoryRepository` y las convierte en objetos `MemoryResponse`.

### Motivo

Separar la entidad del contrato HTTP evita acoplar directamente la estructura de la base de datos con la respuesta que consume el frontend.

Esta separación permite utilizar nombres internos como `memory_date` y `file_path` en PostgreSQL, mientras la API conserva los campos actuales:

* `date`.
* `file`.

El frontend no necesitó cambios porque el contrato JSON se mantuvo sin modificaciones.

---

## 26. Archivos multimedia temporales fuera de PostgreSQL

Las fotografías, videos y miniaturas continúan almacenados temporalmente en:

```text
frontend/assets
```

PostgreSQL almacena solamente sus metadatos y rutas relativas.

### Motivo

Este bloque está enfocado en sustituir los datos temporales en memoria por persistencia de metadatos.

Incorporar almacenamiento privado de objetos, subida de archivos o administración multimedia aumentaría innecesariamente la complejidad del MVP.

El almacenamiento privado de archivos se implementará en una etapa futura, sin modificar por ahora el funcionamiento visual del frontend.

---

## 27. Almacenamiento multimedia privado desacoplado del frontend

Las fotografías, videos y miniaturas utilizadas por la aplicación se almacenan fuera de `frontend/assets`.

Durante el desarrollo local, la raíz del almacenamiento se configura mediante:

```properties
app.storage.root=${MEDIA_STORAGE_ROOT}
```

Los archivos de los tres recuerdos actuales se encuentran organizados mediante claves internas con esta estructura:

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

La carpeta local utilizada durante el desarrollo se llama `private-storage` y está excluida de Git mediante `.gitignore`.

### Motivo

Los archivos familiares privados no deben quedar publicados como recursos estáticos del frontend ni almacenarse dentro de PostgreSQL.

Separar los archivos físicos del frontend permite controlar su acceso desde el backend y prepara la arquitectura para sustituir el almacenamiento local por un proveedor privado en el futuro.

La configuración mediante `MEDIA_STORAGE_ROOT` evita escribir rutas absolutas específicas de una computadora dentro del código fuente.

---

## 28. Storage keys internas en PostgreSQL

Las columnas `file_path` y `thumbnail` de la tabla `memories` almacenan claves internas de almacenamiento en lugar de rutas públicas del frontend.

La migración:

```text
V3__replace_media_paths_with_storage_keys.sql
```

reemplaza las rutas anteriores de `frontend/assets` por storage keys independientes del proveedor.

### Motivo

Una storage key identifica un archivo dentro del almacenamiento, pero no expone:

* La ubicación física del servidor.
* Una URL pública.
* La estructura del frontend.
* El proveedor de almacenamiento.
* Credenciales o datos sensibles.

Esto permite cambiar la implementación del almacenamiento sin modificar los registros de PostgreSQL ni acoplar la base de datos a un proveedor específico.

---

## 29. Servicio de almacenamiento local con protección de rutas

El backend define la abstracción `MediaStorageService` para cargar archivos mediante una storage key.

La implementación inicial `LocalMediaStorageService` resuelve los archivos dentro de la raíz configurada por `MEDIA_STORAGE_ROOT`.

Antes de devolver un archivo, el servicio normaliza la ruta y comprueba que permanezca dentro de la carpeta privada configurada.

### Motivo

La comprobación evita intentos de path traversal mediante claves como:

```text
../outside.jpg
```

La interfaz separa la lógica de recuerdos de la ubicación física de los archivos. Una futura implementación de almacenamiento privado en la nube podrá respetar el mismo contrato sin modificar los controladores ni los servicios de recuerdos.

---

## 30. Entrega de archivos multimedia mediante la API

El backend expone los siguientes endpoints de solo lectura:

```text
GET /api/memories/{id}/file
GET /api/memories/{id}/thumbnail
```

`MemoryMediaService` obtiene el recuerdo desde PostgreSQL, selecciona la storage key correspondiente y solicita el archivo a `MediaStorageService`.

El controlador devuelve:

* `200 OK` con el archivo y su tipo multimedia cuando existe.
* `404 Not Found` cuando el recuerdo o el archivo no existen.

La respuesta pública de `GET /api/memories` y `GET /api/memories/{id}` conserva los campos `file` y `thumbnail`, pero ahora contiene URLs de la API en lugar de storage keys.

### Motivo

El frontend puede continuar utilizando:

```javascript
memory.file
memory.thumbnail
```

sin conocer la ubicación física, la storage key ni el proveedor de almacenamiento.

Esto conserva el contrato JSON existente y mantiene el acceso a los archivos bajo control del backend.

### Validación

La implementación fue validada mediante:

* Pruebas unitarias de `LocalMediaStorageService`.
* Pruebas unitarias de `MemoryMediaService`.
* Pruebas de los endpoints multimedia con MockMvc.
* Suite completa de 19 pruebas con 0 fallos y 0 errores.
* Solicitudes HTTP reales para fotografías, miniaturas y videos.
* Validación manual del frontend con los tres recuerdos.
* Apertura de ambas fotografías en el visor.
* Reproducción del video dentro de la aplicación.
* Ausencia de errores relevantes de CORS o carga multimedia.
