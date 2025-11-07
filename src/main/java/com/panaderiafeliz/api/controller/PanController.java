package com.panaderiafeliz.api.controller;

import com.panaderiafeliz.api.dto.PanDto;
import com.panaderiafeliz.api.model.Pan;
import com.panaderiafeliz.api.service.PanServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/panes")
public class PanController {

    private final PanServicio servicio;

    public PanController(PanServicio servicio) {
        this.servicio = servicio;
    }


    @Operation(
            summary = "Crear pan",
            description = "Registra un nuevo pan con nombre y precio válidos."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pan creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o faltantes"),
            @ApiResponse(responseCode = "409", description = "El nombre ya está registrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<PanDto> crear(@RequestBody PanDto body) {
        PanDto nuevo = servicio.crearPan(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }


    @Operation(
            summary = "Listar panes",
            description = "Devuelve todos los panes o filtra por nombre si se envía el parámetro."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de panes obtenida correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<PanDto>> listar(
            @Parameter(description = "Filtro opcional por nombre (case-insensitive)")
            @RequestParam(required = false) String q) {
        return ResponseEntity.ok(servicio.listarPanes(q));
    }


    @Operation(
            summary = "Obtener pan por ID",
            description = "Busca un pan por su identificador único."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pan encontrado"),
            @ApiResponse(responseCode = "404", description = "Pan no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PanDto> obtener(
            @Parameter(description = "ID del pan a buscar")
            @PathVariable Long id) {
        PanDto pan = servicio.obtenerPan(id);
        return ResponseEntity.ok(pan);
    }


    @Operation(
            summary = "Actualizar pan",
            description = "Modifica el nombre y/o el precio de un pan existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pan actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o faltantes"),
            @ApiResponse(responseCode = "404", description = "Pan no encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflicto de nombre duplicado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PanDto> actualizar(
            @Parameter(description = "ID del pan a actualizar")
            @PathVariable Long id,
            @RequestBody PanDto body) {
        PanDto actualizado = servicio.actualizarPan(id, body);
        return ResponseEntity.ok(actualizado);
    }


    @Operation(
            summary = "Eliminar pan",
            description = "Elimina un pan existente por su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pan eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Pan no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del pan a eliminar")
            @PathVariable Long id) {
        servicio.eliminarPan(id);
        return ResponseEntity.noContent().build();
    }
}
