package com.pruebas.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pruebas.app.models.dao.IProductoDao;
import com.pruebas.app.models.entity.Producto;

@Service
public class ProductoServiceImpl implements IProductoService {

	@Autowired
	public IProductoDao productoDao;

	@Override
	@Transactional(readOnly = true)
	public List<Producto> findByNombre(String term) {
		return this.productoDao.findByNombreLikeIgnoreCase("%" + term + "%");
	}

	@Override
	@Transactional(readOnly = true)
	public Producto findById(Long id) {
		return this.productoDao.findById(id).orElse(null);
	}

}
