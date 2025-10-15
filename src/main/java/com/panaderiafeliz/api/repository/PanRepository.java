package com.panaderiafeliz.api.repository;

import com.panaderiafeliz.api.model.Pan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PanRepository extends JpaRepository<Pan, Long> {
    boolean existsByNombreIgnoreCase(String nombre);
    List<Pan> findByNombreContainingIgnoreCase(String q);
}