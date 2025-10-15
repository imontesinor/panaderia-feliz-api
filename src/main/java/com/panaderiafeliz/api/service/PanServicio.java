package com.panaderiafeliz.api.service;

import com.panaderiafeliz.api.model.Pan;
import com.panaderiafeliz.api.repository.PanRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class PanServicio {

    private static final String MSG_NOT_FOUND = "Pan no encontrado";
    private static final String MSG_DUPLICATE = "El nombre ya está registrado";

    private final PanRepository repo;

    public PanServicio(PanRepository repo) {
        this.repo = repo;
    }

    public ResponseEntity<?> crearPan(Pan body) {
        var err = validar(body);
        if (err != null) return err;
        if (repo.existsByNombreIgnoreCase(body.getNombre())) return conflicto(MSG_DUPLICATE);
        try {
            return created(repo.save(body));
        } catch (DataIntegrityViolationException e) {
            return conflicto("No se pudo guardar por integridad de datos");
        } catch (Exception e) {
            return interno();
        }
    }

    public ResponseEntity<?> listarPanes(String q) {
        List<Pan> data = (q == null || q.isBlank())
                ? repo.findAll()
                : repo.findByNombreContainingIgnoreCase(q.trim());
        return ok(data);
    }

    public ResponseEntity<?> obtenerPan(Long id) {
        return repo.findById(id)
                .<ResponseEntity<?>>map(this::ok)
                .orElseGet(() -> noEncontrado(MSG_NOT_FOUND));
    }

    public ResponseEntity<?> actualizarPan(Long id, Pan body) {
        var opt = repo.findById(id);
        if (opt.isEmpty()) return noEncontrado(MSG_NOT_FOUND);

        var err = validar(body);
        if (err != null) return err;

        Pan actual = opt.get();
        boolean cambiaNombre = !body.getNombre().equalsIgnoreCase(actual.getNombre());
        if (cambiaNombre && repo.existsByNombreIgnoreCase(body.getNombre())) return conflicto(MSG_DUPLICATE);

        try {
            actual.setNombre(body.getNombre());
            actual.setPrecio(body.getPrecio());
            return ok(repo.save(actual));
        } catch (DataIntegrityViolationException e) {
            return conflicto("No se pudo actualizar por integridad de datos");
        } catch (Exception e) {
            return interno();
        }
    }

    public ResponseEntity<?> eliminarPan(Long id) {
        if (!repo.existsById(id)) return noEncontrado(MSG_NOT_FOUND);
        try {
            repo.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return interno();
        }
    }


    private ResponseEntity<?> validar(Pan p) {
        if (p == null) return badRequest("Cuerpo de la petición vacío");
        String nombre = trim(p.getNombre());
        BigDecimal precio = p.getPrecio();

        if (nombre == null || nombre.isBlank()) return badRequest("El nombre es obligatorio");
        if (nombre.length() > 60)             return badRequest("El nombre no debe superar 60 caracteres");
        if (precio == null)                   return badRequest("El precio es obligatorio");
        if (precio.scale() > 2)               return badRequest("El precio debe tener como máximo 2 decimales");
        if (precio.compareTo(new BigDecimal("0.01")) < 0) return badRequest("El precio debe ser mayor a 0");

        p.setNombre(nombre);
        return null;
    }

    private String trim(String s){ return s == null ? null : s.trim(); }
    private ResponseEntity<Map<String,String>> badRequest(String m){ return ResponseEntity.badRequest().body(Map.of("message", m)); }
    private ResponseEntity<Map<String,String>> conflicto(String m){ return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", m)); }
    private ResponseEntity<Map<String,String>> noEncontrado(String m){ return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", m)); }
    private ResponseEntity<Map<String,String>> interno(){ return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message","Error interno")); }
    private <T> ResponseEntity<T> ok(T body){ return ResponseEntity.ok(body); }
    private <T> ResponseEntity<T> created(T body){ return ResponseEntity.status(HttpStatus.CREATED).body(body); }
}
