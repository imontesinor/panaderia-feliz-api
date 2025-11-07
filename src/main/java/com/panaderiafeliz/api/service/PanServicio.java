package com.panaderiafeliz.api.service;

import com.panaderiafeliz.api.dto.PanDto;
import com.panaderiafeliz.api.model.Pan;

import java.util.List;

public interface PanServicio {
  public PanDto crearPan(PanDto body);
  public  List<PanDto> listarPanes(String q);
  public  PanDto obtenerPan(Long id);
  public   PanDto actualizarPan(Long id, PanDto body);
  public  void eliminarPan(Long id);
}

