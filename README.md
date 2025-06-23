# Proyecto Full-Stack: API de Películas, Series y Géneros

## Descripción General de la Solución

Este proyecto es una aplicación full-stack diseñada para gestionar y visualizar una colección de películas, series y géneros. Consiste en un **Backend (API Rest)** desarrollado con Java y Spring Boot, y un **Frontend (Interfaz Web)** desarrollado con Angular.

El objetivo es proporcionar una interfaz RESTful para interactuar con datos persistidos en una base de datos relacional (PostgreSQL), permitiendo operaciones de listado paginado y ordenado, así como la consulta de detalles de entidades individuales. El frontend consume estos servicios y presenta la información en tablas interactivas con funcionalidades de paginación, búsqueda y ordenamiento.

### Funcionalidades Clave

* **Backend:**
    * API Rest para `Películas`, `Series` y `Géneros`.
    * Listado paginado y ordenado de entidades (`GET /api/entidades`).
    * Detalle de entidad por ID (`GET /api/entidades/{id}`).
    * Manejo de dependencias entre entidades (ej. Películas y Series con Géneros).
    * Validación de entradas y manejo de errores con códigos HTTP adecuados.
    * Pruebas unitarias con JUnit 5 y Mockito.
    * Uso de logging con SLF4J/Logback (o Log4j2).
* **Frontend:**
    * Interfaz web desarrollada con Angular (versión LTS actual).
    * Consumo de servicios Rest del backend.
    * Tablas para `Películas`, `Series` y `Géneros` con paginación, búsqueda y ordenamiento.
    * Pruebas unitarias mínimas (Jasmine/Karma).

## Consideraciones de Arquitectura y Decisiones Técnicas

### Backend
* **Java 17+ y Spring Boot 3+:** Se eligieron estas versiones para aprovechar las últimas características y mejoras de rendimiento y seguridad del ecosistema Java y Spring.
* **Spring Data JPA:** Facilita el acceso a datos y reduce el código boilerplate para operaciones CRUD.
* **PostgreSQL:** Seleccionada como base de datos relacional por su robustez, escalabilidad y amplio soporte.
* **Estructura de Capas:** El backend sigue una arquitectura en capas (Controladores, Servicios, Repositorios) para una clara separación de responsabilidades y modularidad.
* **Manejo de Paginación y Ordenamiento:** Se utiliza `Spring Data JPA Pageable` para manejar eficientemente la paginación y el ordenamiento directamente desde la base de datos, optimizando el rendimiento para grandes volúmenes de datos.
* **CORS:** Se ha configurado CORS globalmente (o a nivel de controlador) para permitir las solicitudes desde el dominio del frontend (`http://localhost:4200`).

### Frontend
* **Angular (versión LTS actual):** Framework robusto para construir Single Page Applications (SPAs), elegido por su ecosistema maduro, tooling y facilidad para crear componentes reutilizables.
* **Angular Material:** Utilizado para los componentes de UI (tablas, paginadores, botones, formularios) para proporcionar una interfaz de usuario consistente y atractiva siguiendo las directrices de Material Design.
* **Estructura Modular:** El frontend se organiza en módulos lógicos (ej. `generos`, `peliculas`, `series`, `core`, `shared`) para mejorar la mantenibilidad, escalabilidad y la claridad del código.
* **Servicios:** Los servicios (`PeliculaService`, `SerieService`, `GeneroService`, `ApiService`) encapsulan la lógica de comunicación con el backend, manteniendo los componentes limpios y enfocados en la UI.
* **Enrutamiento:** Se utiliza el módulo de enrutamiento de Angular para gestionar la navegación entre las diferentes vistas de la aplicación (listados, formularios).

## Instrucciones para Compilar, Ejecutar Tests y Arrancar el Ambiente

Asegúrate de tener **Java 17+**, **Maven** y **Node.js (con npm)** instalados en tu sistema.

### 1. Configuración de la Base de Datos (PostgreSQL)

Antes de iniciar el backend, asegúrate de tener una instancia de PostgreSQL accesible.

* Crea una base de datos con el nombre `movies_series_db`.
* Asegúrate de que las credenciales en `src/main/resources/application.properties` (o `application.yml`) de tu backend coincidan con las de tu base de datos:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/movies_series_db
    spring.datasource.username=tu_usuario_db
    spring.datasource.password=tu_password_db
    spring.jpa.hibernate.ddl-auto=update # o create para la primera vez si no usas migraciones
    ```
* La data inicial se cargará automáticamente al iniciar la aplicación si tienes scripts SQL (`data.sql`, `schema.sql`) en `src/main/resources` o si has configurado migraciones (Ej. Liquibase).

### 2. Arranque del Backend

Navega a la carpeta raíz del proyecto backend (`movies-series-api` o similar, donde se encuentra el `pom.xml`).

```bash
# Compilar el proyecto
./mvnw clean install

# Ejecutar la aplicación Spring Boot
./mvnw spring-boot:run# prueba-java