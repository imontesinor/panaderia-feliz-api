package com.panaderiafeliz.api.service;

import com.panaderiafeliz.api.model.Pan;

import java.util.List;

public interface PanServicio {
  public  Pan crearPan(Pan body);
  public  List<Pan> listarPanes(String q);
  public  Pan obtenerPan(Long id);
  public   Pan actualizarPan(Long id, Pan body);
  public  void eliminarPan(Long id);
}

