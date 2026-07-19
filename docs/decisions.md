# Decisiones técnicas

## 1. Frontend sin framework

Se utilizarán HTML, CSS y JavaScript sin React o Vue durante la primera etapa.

### Motivo

El MVP es pequeño y no requiere todavía la complejidad de un framework.

## 2. Datos locales

Los recuerdos se definirán inicialmente en un archivo JSON.

### Motivo

Esto permite validar la interfaz antes de construir un backend y una base de datos.

## 3. Archivos de prueba

Durante el desarrollo se utilizarán copias de fotografías y videos.

### Motivo

Los archivos originales deben mantenerse fuera del proyecto y contar con respaldo.

## 4. Archivos multimedia fuera de Git

Las fotografías y videos estarán ignorados mediante `.gitignore`.

### Motivo

Los recuerdos son privados y pueden ocupar demasiado espacio.

## 5. Estructura semántica de la pantalla principal

La pantalla principal utiliza elementos HTML semánticos como `header`, `main`,
`section`, `nav`, `article` y `footer`.

### Motivo

Una estructura semántica mejora la accesibilidad, facilita el mantenimiento y
permite que tecnologías de asistencia comprendan mejor el contenido.

## 6. Diseño accesible y adaptable

La interfaz utiliza texto de tamaño amplio, tarjetas con áreas táctiles grandes,
contraste visible y estados de enfoque para navegación mediante teclado.

El diseño comienza con una columna para teléfonos y aumenta progresivamente a
dos o cuatro columnas según el espacio disponible.

### Motivo

La usuaria principal tiene poca experiencia tecnológica. La interfaz debe ser
fácil de leer, tocar y comprender desde un teléfono o una tableta.

## 7. Generación dinámica de recuerdos

Las tarjetas de recuerdos se generan con JavaScript a partir del archivo
`frontend/data/memories.json`.

### Motivo

Separar los datos del HTML permite agregar, editar o eliminar recuerdos sin
modificar directamente la estructura de la página.

También prepara el frontend para consumir una API REST en una etapa futura.


## 8. Uso de copias multimedia durante el desarrollo

Durante el desarrollo se utilizan copias de fotografías y videos dentro de
`frontend/assets`.

Estos archivos están excluidos del repositorio mediante `.gitignore`.

### Motivo

Los recuerdos familiares son privados y pueden tener un tamaño considerable.
Los archivos originales deben mantenerse fuera del proyecto y contar con al
menos un respaldo independiente.

## 9. Visor interno de fotografías

Las fotografías se abren dentro de la misma aplicación mediante un visor
dedicado.

El visor muestra la imagen, el título, la fecha, el lugar y la descripción del
recuerdo. También incluye un botón visible para regresar a la galería.

### Motivo

Abrir la fotografía directamente como archivo puede desorientar a la usuaria.
Mantener la navegación dentro de la aplicación ofrece una experiencia más
predecible y sencilla.

## 10. Reproductor interno de videos

Los videos se reproducen dentro de la aplicación mediante el elemento HTML
`video`.

El reproductor incluye controles nativos del navegador, información del
recuerdo y un botón visible para regresar a la galería.

Al cerrar el visor, el video se pausa y se elimina temporalmente su fuente.

### Motivo

Mantener la reproducción dentro de la aplicación evita que la usuaria salga del
flujo principal o se desoriente al abrir el archivo directamente.

## 11. Filtros de categorías en el frontend

Las categorías de la pantalla principal filtran los recuerdos cargados desde
`memories.json`.

Los datos originales se mantienen en memoria y la galería se vuelve a renderizar
según la categoría seleccionada.

La categoría activa se comunica visualmente y mediante el atributo
`aria-current`.

### Motivo

El filtrado permite encontrar recuerdos con menos pasos y sin presentar
formularios o controles complejos a la usuaria.

## 12. Uso de módulos JavaScript nativos

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

## 13. Componente modular para tarjetas de recuerdos

La creación y renderización de las tarjetas se trasladó al módulo
`js/components/memory-card.js`.

El componente recibe funciones para abrir fotografías y videos, por lo que no
controla directamente los visores.

### Motivo

Separar la presentación de las tarjetas reduce las responsabilidades de
`app.js` y evita acoplar el componente con la navegación de la aplicación.