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

---

## 31. Paginación y filtrado por categoría desde la API

El endpoint `GET /api/memories` devuelve ahora una respuesta paginada.

Acepta estos parámetros opcionales:

* `page`
* `size`
* `category`

Ejemplos:

```text
GET /api/memories?page=0&size=6
GET /api/memories?page=0&size=6&category=viajes
```

La numeración de páginas comienza en `0`.

El tamaño predeterminado es `6` y el máximo permitido es `24`.

Los recuerdos se ordenan de forma estable mediante:

```text
memory_date DESC
id DESC
```

La respuesta utiliza el DTO `PagedMemoryResponse` con estos campos:

* `content`
* `page`
* `size`
* `totalElements`
* `totalPages`
* `hasNext`

Cada elemento de `content` conserva el contrato actual de `MemoryResponse`.

El filtrado por categoría ahora se realiza desde la API.

El frontend reinicia la paginación al cambiar de categoría y muestra el botón accesible `Ver más recuerdos` cuando existen páginas adicionales.

No se implementó scroll infinito.

### Motivo

La paginación permite que la aplicación crezca sin cargar todos los recuerdos en una sola solicitud.

El filtrado desde PostgreSQL evita descargar recuerdos de categorías que la usuaria no seleccionó.

El orden por fecha e identificador evita cambios impredecibles entre páginas.

El DTO propio evita exponer directamente tipos internos de Spring.

### Validación

* Pruebas del repositorio para paginación, filtrado y orden estable.
* Pruebas MockMvc para el contrato paginado.
* Pruebas de valores predeterminados y límite máximo.
* Suite completa de 24 pruebas con 0 fallos y 0 errores.
* Solicitudes HTTP reales con y sin categoría.
* Prueba temporal con páginas de 2 recuerdos.
* Reinicio de paginación al cambiar de categoría.
* Validación manual de fotografías, videos y miniaturas.


---

## 32. Contrato del manifiesto CSV para validación de importaciones

La futura importación masiva utilizará un manifiesto CSV ubicado dentro de una carpeta externa configurada mediante `IMPORT_ROOT`.

El manifiesto describirá los metadatos y los archivos candidatos de cada recuerdo.

No contendrá rutas físicas absolutas ni storage keys definitivas de `private-storage`.

Las columnas obligatorias, en su orden exacto, serán:

```csv
id,title,type,category,date,place,file,thumbnail,description
```

### Reglas de los campos

* `id`: número entero positivo y único dentro del manifiesto.
* `title`: texto obligatorio con un máximo de 150 caracteres.
* `type`: únicamente `photo` o `video`.
* `category`: únicamente `viajes`, `familia` o `celebraciones` durante el MVP.
* `date`: fecha obligatoria en formato ISO `YYYY-MM-DD`.
* `place`: texto obligatorio con un máximo de 100 caracteres.
* `file`: ruta relativa al archivo principal dentro de `IMPORT_ROOT`.
* `thumbnail`: ruta relativa a la miniatura dentro de `IMPORT_ROOT`.
* `description`: texto obligatorio con un máximo de 500 caracteres.

Los campos de texto no podrán quedar vacíos después de eliminar espacios al inicio y al final.

El encabezado deberá contener todas las columnas obligatorias en el orden establecido.

No se permitirán columnas faltantes, desconocidas o repetidas.

### Fotografías y videos

Para una fotografía:

* `type` deberá ser `photo`.
* `file` deberá señalar un archivo de imagen admitido.
* `thumbnail` podrá contener la misma ruta relativa que `file`.

Para un video:

* `type` deberá ser `video`.
* `file` deberá señalar un archivo de video admitido.
* `thumbnail` deberá señalar una imagen separada.

Las extensiones y los tipos admitidos serán validados antes de considerar válida una fila.

### Rutas de origen

Los campos `file` y `thumbnail` representarán rutas relativas dentro de `IMPORT_ROOT`.

Ejemplos válidos:

```text
photos/viaje-familiar.jpg
videos/cumpleanos-familiar.mp4
thumbnails/cumpleanos-familiar.jpg
```

El manifiesto no utilizará storage keys definitivas como:

```text
memories/10/photo/viaje-familiar.jpg
```

Las storage keys definitivas pertenecerán a una futura etapa de importación real y no serán generadas durante el `dry-run`.

### Seguridad de rutas

Las rutas de `file` y `thumbnail` se resolverán exclusivamente dentro de `IMPORT_ROOT`.

No se permitirán:

* Rutas absolutas.
* Rutas vacías.
* Segmentos que escapen de la raíz mediante `..`.
* Archivos inexistentes.
* Directorios utilizados como archivos.
* Resoluciones que terminen fuera de la raíz configurada.

La carpeta de importación permanecerá separada de `private-storage`.

El proceso deberá normalizar las rutas antes de comprobar que continúan dentro de `IMPORT_ROOT`.

### Duplicados dentro del manifiesto

El `dry-run` detectará:

* Identificadores repetidos.
* Filas completamente repetidas.
* Archivos principales reutilizados por recuerdos diferentes.
* Miniaturas reutilizadas por recuerdos diferentes.
* Una misma ruta utilizada de manera incompatible.

La repetición de la misma ruta en `file` y `thumbnail` dentro de una única fila de tipo `photo` será válida y no se considerará un duplicado.

La detección de duplicados se limitará inicialmente al contenido del manifiesto.

El `dry-run` no consultará PostgreSQL para detectar identificadores o archivos que ya existan en la colección definitiva.

### Reporte de validación

El resultado del proceso será un reporte de solo lectura.

El reporte deberá indicar como mínimo:

* Si el manifiesto es válido o inválido.
* Cantidad total de filas procesadas.
* Cantidad de filas válidas.
* Cantidad de filas inválidas.
* Errores generales del manifiesto.
* Número de fila asociado a cada problema.
* Campo relacionado con el problema, cuando corresponda.
* Mensaje claro que explique la causa.

Una fila podrá contener más de un problema.

El reporte deberá recopilar todos los problemas detectables en una ejecución, en lugar de detenerse después del primer error.

### Límites del dry-run

La validación no realizará ninguna de las siguientes operaciones:

* Copiar archivos.
* Mover archivos.
* Renombrar archivos.
* Eliminar archivos.
* Modificar `private-storage`.
* Insertar registros en PostgreSQL.
* Actualizar registros en PostgreSQL.
* Generar storage keys definitivas.
* Generar miniaturas.
* Leer metadatos EXIF.
* Limpiar metadatos EXIF.
* Convertir u optimizar archivos multimedia.
* Modificar la API.
* Modificar el frontend.

El proceso trabajará únicamente con archivos de prueba copiados dentro de la carpeta externa de importación.

### Motivo

Separar las rutas de origen de las storage keys definitivas evita acoplar el manifiesto al proveedor o a la estructura final de almacenamiento.

Definir el contrato antes de implementar el lector reduce ambigüedades y permite crear pruebas automatizadas precisas.

El modo `dry-run` proporciona una barrera de seguridad antes de cualquier futura operación masiva sobre archivos o datos.

### Validación

La implementación fue validada mediante:

* Suite completa de 65 pruebas automatizadas.
* 0 fallos.
* 0 errores.
* `BUILD SUCCESS`.
* Pruebas del enlace de `IMPORT_ROOT`.
* Pruebas de lectura segura del manifiesto CSV.
* Pruebas de encabezados faltantes, adicionales, desordenados y repetidos.
* Pruebas de campos obligatorios y límites de longitud.
* Pruebas de identificadores positivos.
* Pruebas de fechas ISO válidas y fechas inexistentes.
* Pruebas de tipos y categorías permitidos.
* Pruebas de rutas absolutas, path traversal y archivos inexistentes.
* Pruebas de archivos regulares y extensiones admitidas.
* Pruebas de fotografías y videos con miniaturas.
* Pruebas de identificadores, filas y rutas repetidas.
* Pruebas del reporte consolidado del `dry-run`.
* Validación manual con un manifiesto de 4 recuerdos.
* Uso de 6 archivos copiados dentro de una carpeta externa fuera del repositorio.
* Reporte manual con 4 filas válidas y 0 filas inválidas.
* Eliminación de la prueba temporal después de la comprobación.
* Repositorio limpio al finalizar.

La validación manual confirmó que el proceso no modificó PostgreSQL, `private-storage`, la API ni el frontend.

## 33. Detección de archivos duplicados por contenido mediante SHA-256

El `dry-run` calculará un hash SHA-256 para cada archivo principal y miniatura válidos encontrados dentro de `IMPORT_ROOT`.

El objetivo será detectar archivos que tengan contenido binario idéntico aunque utilicen nombres o rutas relativas diferentes.

La detección se aplicará entre:

* Dos archivos principales.
* Dos miniaturas.
* Un archivo principal y una miniatura.
* Archivos pertenecientes a filas diferentes del manifiesto.

Cuando un hash ya haya aparecido anteriormente, el reporte asociará el problema con la aparición posterior e indicará:

* Número de fila.
* Campo afectado.
* Campo donde apareció primero.
* Fila de la primera aparición.

El mensaje utilizado será:

`El archivo tiene contenido duplicado; apareció primero en el campo <campo> de la fila <fila>.`

### Separación de responsabilidades

La reutilización exacta de una misma ruta física continuará siendo responsabilidad de `ImportManifestDuplicateValidator`.

`ImportContentDuplicateValidator` se concentrará en rutas físicas diferentes cuyo contenido produzca el mismo hash SHA-256.

Esto evita generar dos problemas diferentes para una única reutilización de ruta.

Una fotografía podrá continuar usando exactamente su propio archivo principal como miniatura.

### Resolución segura de rutas

La resolución de rutas se centraliza en `ImportMediaPathResolver`.

Este servicio valida que cada archivo:

* Utilice una ruta relativa.
* Permanezca dentro de `IMPORT_ROOT`.
* Exista.
* Sea un archivo regular.
* No termine fuera de la raíz mediante path traversal o enlaces simbólicos.

`ImportMediaFileValidator` y `ImportContentDuplicateValidator` reutilizan esta resolución común para mantener las mismas reglas de seguridad.

### Cálculo y reutilización de hashes

`ImportFileHashCalculator` calcula SHA-256 mediante lectura secuencial del archivo.

Los archivos se procesan con un buffer y no se cargan completamente en memoria.

Cada ejecución del `dry-run` mantiene una caché local con la relación:

`ruta física real → hash SHA-256`

La caché pertenece únicamente a esa ejecución.

No se almacena como estado permanente de un servicio singleton y no se reutiliza entre ejecuciones diferentes.

Esto evita:

* Leer varias veces el mismo archivo físico dentro de un reporte.
* Conservar hashes obsoletos después de que cambie un archivo.
* Introducir estado compartido entre ejecuciones.

### Fallos durante el cálculo

Si un archivo válido deja de poder leerse durante el cálculo del hash, el `dry-run` no abortará todo el proceso.

El campo correspondiente recibirá el problema:

`No fue posible calcular el hash SHA-256 del archivo.`

Un fallo interno de la JVM que impida disponer del algoritmo SHA-256 continuará propagándose como error de aplicación.

### Límites

La detección por contenido permanece dentro del modo de solo lectura.

No realizará ninguna de las siguientes operaciones:

* Copiar archivos.
* Mover archivos.
* Renombrar archivos.
* Eliminar archivos.
* Modificar `private-storage`.
* Insertar o actualizar registros en PostgreSQL.
* Generar storage keys definitivas.
* Generar miniaturas.
* Leer o limpiar metadatos EXIF.
* Modificar la API.
* Modificar el frontend.

El hash se utiliza exclusivamente para producir el reporte del `dry-run`.

No se persiste en PostgreSQL ni se incorpora todavía al modelo definitivo de recuerdos.

### Motivo

Comparar únicamente nombres y rutas no permite detectar copias del mismo archivo guardadas con nombres diferentes.

SHA-256 proporciona una identificación determinista del contenido y permite detectar esas copias antes de una futura importación masiva.

La caché local reduce lecturas innecesarias sin introducir estado persistente o compartido.

Mantener separadas la validación estructural de rutas y la validación por contenido produce mensajes más claros y evita responsabilidades duplicadas.

### Validación

La implementación fue validada mediante:

* Suite completa de 85 pruebas automatizadas.
* 0 fallos.
* 0 errores.
* `BUILD SUCCESS`.
* 61 pruebas dentro del módulo `importvalidation`.
* Pruebas del cálculo exacto de SHA-256.
* Pruebas de reutilización del hash para una misma ruta física.
* Pruebas que confirman el recálculo al utilizar una caché nueva.
* Pruebas con archivos de contenido diferente.
* Pruebas con archivos principales de contenido idéntico.
* Pruebas con miniaturas de contenido idéntico.
* Pruebas de contenido reutilizado entre archivo principal y miniatura.
* Prueba de fotografía usando su propio archivo como miniatura.
* Prueba de una misma ruta física reutilizada sin generar un segundo problema por contenido.
* Pruebas de rutas inválidas o inexistentes.
* Prueba de fallo de lectura convertido en `ImportValidationIssue`.
* Prueba integrada del reporte con dos rutas diferentes y contenido idéntico.
* Prueba integrada que acepta archivos con contenido diferente.
* Ejecución de `./mvnw clean test`.

La validación confirmó que la funcionalidad no modifica PostgreSQL, `private-storage`, la API ni el frontend.
