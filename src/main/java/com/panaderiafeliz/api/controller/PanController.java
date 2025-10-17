package com.panaderiafeliz.api.controller;

import com.panaderiafeliz.api.model.Pan;
import com.panaderiafeliz.api.service.PanServicio;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/panes")
public class PanController {

    private final PanServicio servicio;

    public PanController(PanServicio servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    @Operation(summary = "Crear Panes", description = "Registrar un nuevo pan")
    public ResponseEntity<?> crear(@RequestBody Pan body) {
        return servicio.crearPan(body);
    }

    @GetMapping
    @Operation(summary = "Listar panes", description = "Obtiene todos los panes o filtra por nombre")
    public ResponseEntity<?> listar(@RequestParam(required = false) String q) {
        return servicio.listarPanes(q);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Panes", description = "Consulta mediante el id Busca de un pan espefico")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        return servicio.obtenerPan(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar Panes", description = "Modificar nombre y/o precio")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Pan body) {
        return servicio.actualizarPan(id, body);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar Panes", description = "Borrar un pan por id")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        return servicio.eliminarPan(id);
    }
}
