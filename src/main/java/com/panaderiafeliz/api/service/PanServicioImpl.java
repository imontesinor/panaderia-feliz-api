package com.panaderiafeliz.api.service;

import com.panaderiafeliz.api.dto.PanDto;
import com.panaderiafeliz.api.mapper.PanMapper;
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

    private final PanMapper mapper;

    public PanServicioImpl(PanRepository repo,PanMapper mapper) {
        this.repo = repo;
        this.mapper=mapper;
    }

    @Override
    public PanDto crearPan(PanDto body) {

        validar(body);
        if (repo.existsByNombreIgnoreCase(body.getTitulo())) {
            throw new IllegalArgumentException(MSG_DUPLICATE);
        }
        try {
            Pan pan = mapper.toEntity(body);
            Pan save = repo.save(pan);
           return mapper.toDto(save);
        } catch (DataIntegrityViolationException e) {
            throw e;
        }
    }

    @Override
    public List<PanDto> listarPanes(String q) {
        List<Pan> pan = (q == null || q.isBlank())
                ? repo.findAll()
                : repo.findByNombreContainingIgnoreCase(q.trim());
        return  pan.stream().map(mapper :: toDto).toList();
    }

    @Override
    public PanDto obtenerPan(Long id) {

        Pan pan =repo.findById(id).orElseThrow(() -> new NoSuchElementException(MSG_NOT_FOUND));

        return mapper.toDto(pan);
    }

    @Override
    public PanDto actualizarPan(Long id, PanDto body) {
        Pan actual = repo.findById(id).orElseThrow(() -> new NoSuchElementException(MSG_NOT_FOUND));
        validar(body);

        boolean cambiaNombre = !body.getTitulo().equalsIgnoreCase(actual.getNombre());
        if (cambiaNombre && repo.existsByNombreIgnoreCase(body.getTitulo())) {
            throw new IllegalArgumentException(MSG_DUPLICATE);
        }

        try {
            actual.setNombre(body.getTitulo());
            actual.setPrecio(body.getPrecio());
            Pan pan = repo.save(actual);
            return mapper.toDto(pan);


        } catch (DataIntegrityViolationException e) {
            throw e;
        }
    }

    @Override
    public void eliminarPan(Long id) {
        if (!repo.existsById(id)) throw new NoSuchElementException(MSG_NOT_FOUND);
        repo.deleteById(id);
    }


    private void validar(PanDto p) {
        if (p == null) throw new IllegalArgumentException("Cuerpo de la petición vacío");
        String nombre = p.getTitulo() == null ? null : p.getTitulo().trim();
        BigDecimal precio = p.getPrecio();

        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre es obligatorio");
        if (nombre.length() > 60)             throw new IllegalArgumentException("El nombre no debe superar 60 caracteres");
        if (precio == null)                   throw new IllegalArgumentException("El precio es obligatorio");
        if (precio.scale() > 2)               throw new IllegalArgumentException("El precio debe tener como máximo 2 decimales");
        if (precio.compareTo(new BigDecimal("0.01")) < 0)
            throw new IllegalArgumentException("El precio debe ser mayor a 0");

        p.setTitulo(nombre);
    }
}
