# Panaderia API

servicio REST para gestionar panes (CRUD) con Spring Boot + JPA sobre H2 en memoria.

## Arranque
- Requisitos: Java 17, Maven
- Ejecutar: `mvnw spring-boot:run`
- H2 Console: `http://localhost:8080/h2-console`
    - JDBC: `jdbc:h2:mem:panaderia`
    - User: `sa` (sin password)

## Endpoints
- `POST /api/panes`
- `GET  /api/panes` (filtro `?q=` opcional)
- `GET  /api/panes/{id}`
- `PUT  /api/panes/{id}`
- `DELETE /api/panes/{id}`

## Validaciones
- nombre obligatorio, máx. 60, único (case-insensitive)
- precio obligatorio, > 0, máx. 2 decimales

## Ejemplo JSON: crear pan (POST)
```json
{ "nombre": "Pan de queso", "precio": 1000.00 }
