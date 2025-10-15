package com.panaderiafeliz.api.controller;

import com.panaderiafeliz.api.model.Pan;
import com.panaderiafeliz.api.service.PanServicio;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/panes")
public class PanController {

    private final PanServicio servicio;

    public PanController(PanServicio servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Pan body) {
        return servicio.crearPan(body);
    }

    @GetMapping
    public ResponseEntity<?> listar(@RequestParam(required = false) String q) {
        return servicio.listarPanes(q);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        return servicio.obtenerPan(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Pan body) {
        return servicio.actualizarPan(id, body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        return servicio.eliminarPan(id);
    }
}
