package com.pruebas.app.models.service;

import java.util.List;

import com.pruebas.app.models.entity.Producto;

public interface IProductoService {

	public List<Producto> findByNombre(String term);

	public Producto findById(Long id);

}
