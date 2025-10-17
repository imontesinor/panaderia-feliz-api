package com.panaderiafeliz.api.service;

import com.panaderiafeliz.api.model.Pan;
import com.panaderiafeliz.api.repository.PanRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PanServicioImpl implements PanServicio {

    private static final String MSG_NOT_FOUND = "Pan no encontrado";
    private static final String MSG_DUPLICATE = "El nombre ya está registrado";

    private final PanRepository repo;

    public PanServicioImpl(PanRepository repo) {
        this.repo = repo;
    }

    @Override
    public Pan crearPan(Pan body) {
        validar(body);
        if (repo.existsByNombreIgnoreCase(body.getNombre())) {
            throw new IllegalArgumentException(MSG_DUPLICATE);
        }
        try {
            return repo.save(body);
        } catch (DataIntegrityViolationException e) {
            throw e;
        }
    }

    @Override
    public List<Pan> listarPanes(String q) {
        return (q == null || q.isBlank())
                ? repo.findAll()
                : repo.findByNombreContainingIgnoreCase(q.trim());
    }

    @Override
    public Pan obtenerPan(Long id) {
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException(MSG_NOT_FOUND));
    }

    @Override
    public Pan actualizarPan(Long id, Pan body) {
        Pan actual = repo.findById(id).orElseThrow(() -> new NoSuchElementException(MSG_NOT_FOUND));
        validar(body);

        boolean cambiaNombre = !body.getNombre().equalsIgnoreCase(actual.getNombre());
        if (cambiaNombre && repo.existsByNombreIgnoreCase(body.getNombre())) {
            throw new IllegalArgumentException(MSG_DUPLICATE);
        }

        try {
            actual.setNombre(body.getNombre());
            actual.setPrecio(body.getPrecio());
            return repo.save(actual);
        } catch (DataIntegrityViolationException e) {
            throw e;
        }
    }

    @Override
    public void eliminarPan(Long id) {
        if (!repo.existsById(id)) throw new NoSuchElementException(MSG_NOT_FOUND);
        repo.deleteById(id);
    }


    private void validar(Pan p) {
        if (p == null) throw new IllegalArgumentException("Cuerpo de la petición vacío");
        String nombre = p.getNombre() == null ? null : p.getNombre().trim();
        BigDecimal precio = p.getPrecio();

        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre es obligatorio");
        if (nombre.length() > 60)             throw new IllegalArgumentException("El nombre no debe superar 60 caracteres");
        if (precio == null)                   throw new IllegalArgumentException("El precio es obligatorio");
        if (precio.scale() > 2)               throw new IllegalArgumentException("El precio debe tener como máximo 2 decimales");
        if (precio.compareTo(new BigDecimal("0.01")) < 0)
            throw new IllegalArgumentException("El precio debe ser mayor a 0");

        p.setNombre(nombre);
    }
}
