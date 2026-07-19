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