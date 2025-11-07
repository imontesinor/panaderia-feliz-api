package com.panaderiafeliz.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class PanDto {

    private Long id;

    @NotBlank(message = "el nombre no puede estar vacio")
    @Size(max=60, message = "el nombre no puede tener mas de 60 caracteres")
    private String titulo;

    @NotNull(message = "el precio es obligatorio")
    @DecimalMin(value= "0.00", message = "el precio debe ser mayo o igual a 0")
    private BigDecimal precio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }
}
