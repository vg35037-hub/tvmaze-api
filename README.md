# TV Maze Middleware API

API middleware en Java con Spring Boot que consume los servicios de TV Maze y agrega una capa de caché con MongoDB Atlas, además de funcionalidad de comentarios y ratings por show.

## Tecnologías

- Java 17
- Spring Boot 3.2.5
- Spring Data MongoDB
- MongoDB Atlas (Free Tier)
- Maven

## Requisitos

- JDK 17
- Cuenta en MongoDB Atlas (cluster gratuito)

## Configuración

1. Crear un cluster gratuito en [MongoDB Atlas](https://www.mongodb.com/atlas)
2. Configurar acceso de red: `0.0.0.0/0` (permitir desde cualquier IP)
3. Crear un usuario de base de datos
4. Editar `src/main/resources/application.properties` con tu connection string:

```properties
spring.data.mongodb.uri=mongodb+srv://TU_USUARIO:TU_PASSWORD@TU_CLUSTER.mongodb.net/tvmaze?retryWrites=true&w=majority
```

## Ejecución

```bash
mvn spring-boot:run
```

La aplicación inicia en `http://localhost:8080`.

## Endpoints

### A - Buscar Shows
```
GET /api/shows/search?q={query}
```
Consume la API de TV Maze (`/search/shows`) y retorna un resumen con: id, name, channel, summary, genres. Si el show tiene comentarios en MongoDB, los incluye.

### B - Obtener Show por ID
```
GET /api/shows/{id}
```
Implementa cache-aside pattern:
1. Busca primero en MongoDB (caché)
2. Si no existe, consulta TV Maze API y guarda en MongoDB
3. Enriquece la respuesta con comentarios si existen

### C - Agregar Comentario
```
POST /api/shows/{id}/comments
Content-Type: application/json

{
  "comment": "Excelente serie",
  "rating": 5
}
```
Validaciones:
- `comment`: obligatorio, no puede estar vacío
- `rating`: obligatorio, entre 0 y 5

## Estructura del Proyecto

```
src/main/java/com/tvmaze/middleware/
├── TvMazeMiddlewareApplication.java
├── client/
│   ├── RestTemplateConfig.java
│   └── TvMazeClient.java
├── controller/
│   └── ShowController.java
├── exception/
│   └── GlobalExceptionHandler.java
├── model/
│   ├── document/
│   │   ├── CommentDocument.java
│   │   └── ShowDocument.java
│   └── dto/
│       ├── CommentDto.java
│       ├── CommentRequestDto.java
│       └── ShowSummaryDto.java
├── repository/
│   ├── CommentRepository.java
│   └── ShowRepository.java
└── service/
    ├── CommentService.java
    └── ShowService.java
```

## Arquitectura

- **Controller**: Endpoints REST, validación de entrada
- **Service**: Lógica de negocio, orquestación cache-aside, enriquecimiento con comentarios
- **Repository**: Acceso a MongoDB via Spring Data
- **Client**: Consumo de TV Maze API externa via RestTemplate
- **Model**: DTOs para respuestas y Documents para MongoDB
- **Exception**: Manejo global de errores con @RestControllerAdvice
