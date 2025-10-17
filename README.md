# Panadería Feliz API

Servicio REST para gestionar panes (CRUD) desarrollado con Spring Boot 3, Spring Data JPA, H2 en memoria y documentado con Swagger / OpenAPI.  
El proyecto implementa una arquitectura en capas, manejo global de excepciones y sigue buenas prácticas de desarrollo backend.

-------------------------------------------------------
ARRANQUE DEL PROYECTO
-------------------------------------------------------
Requisitos: Java 17, Maven
Ejecución:
mvnw spring-boot:run
H2 Console:
URL: http://localhost:8080/h2-console
JDBC: jdbc:h2:mem:panaderia
Usuario: sa (sin password)

-------------------------------------------------------
ENDPOINTS DISPONIBLES
-------------------------------------------------------
POST   /api/panes
GET    /api/panes            (filtro ?q= opcional)
GET    /api/panes/{id}
PUT    /api/panes/{id}
DELETE /api/panes/{id}

-------------------------------------------------------
VALIDACIONES
-------------------------------------------------------
- nombre: obligatorio, máximo 60 caracteres, único (no sensible a mayúsculas)
- precio: obligatorio, mayor a 0, con máximo 2 decimales

Ejemplo JSON para crear un pan:
{
"nombre": "Pan de queso",
"precio": 1000.00
}

-------------------------------------------------------
SWAGGER / OPENAPI
-------------------------------------------------------
La API está documentada automáticamente con springdoc-openapi.
Ruta de acceso:
Swagger UI:    http://localhost:8080/swagger-ui/index.html


-------------------------------------------------------
ARQUITECTURA EN CAPAS
-------------------------------------------------------
El proyecto sigue una arquitectura limpia y organizada:

src/main/java/com/panaderiafeliz/api
│
├── config/
│   ├── OpenApiConfig.java              → configuración Swagger/OpenAPI
│   └── GlobalExceptionHandler.java     → manejo global de excepciones
│
├── controller/
│   └── PanController.java              → expone endpoints REST (CRUD panes)
│
├── model/
│   └── Pan.java                        → entidad JPA representando un pan
│
├── repository/
│   └── PanRepository.java              → acceso a datos con Spring Data JPA
│
├── service/
│   ├── PanServicio.java                → interfaz con operaciones del dominio
│   └── PanServicioImpl.java            → implementación con validaciones
│
└── PanaderiaFelizApiApplication.java   → clase principal de arranque

-------------------------------------------------------
DESCRIPCIÓN DE CADA CAPA
-------------------------------------------------------

CONFIG
Contiene la configuración global:
- OpenApiConfig: define el título, versión y contacto para Swagger.
- GlobalExceptionHandler: centraliza el manejo de errores y genera respuestas JSON coherentes.

CONTROLADOR
En la capa controller se ubican los controladores REST que exponen los endpoints.
Ejemplo: PanController maneja las operaciones CRUD de panes y devuelve ResponseEntity con códigos HTTP adecuados.

MODELO
Define las entidades persistentes de la aplicación.  
Pan.java representa la tabla de panes con validaciones en sus campos.

REPOSITORIO
Contiene las interfaces de acceso a datos.  
PanRepository extiende JpaRepository<Pan, Long> e incluye métodos:
- findByNombreContainingIgnoreCase(String q)
- existsByNombreIgnoreCase(String nombre)

SERVICIO
Encapsula la lógica de negocio:
- PanServicio: define las operaciones CRUD como interfaz.
- PanServicioImpl: implementa validaciones, control de duplicados y reglas de negocio.
  Las excepciones son lanzadas desde esta capa y manejadas globalmente por GlobalExceptionHandler.

-------------------------------------------------------
MANEJO GLOBAL DE EXCEPCIONES
-------------------------------------------------------
Clase: GlobalExceptionHandler.java  
Ubicación: src/main/java/com/panaderiafeliz/api/config

Maneja excepciones comunes y devuelve un JSON estandarizado:

Ejemplo de respuesta:
{
"message": "Pan no encontrado",
"path": "/api/panes/1",
"method": "GET",
"status": 404,
"timestamp": "2025-10-17T16:50:07Z"
}

Excepciones manejadas:
- IllegalArgumentException: 400 Datos inválidos o faltantes
- NoSuchElementException: 404 Pan no encontrado
- DataIntegrityViolationException: 409 Conflicto de integridad (duplicado)
- Exception: 500 Error interno del servidor

-------------------------------------------------------
RESUMEN DE CAPAS Y RESPONSABILIDADES
-------------------------------------------------------
Config: configuraciones globales, Swagger y excepciones
Controller: expone endpoints HTTP
Service: lógica de negocio y validaciones
Repository: persistencia de datos
Model: estructura de entidades JPA


-------------------------------------------------------
