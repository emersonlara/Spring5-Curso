package com.pruebas.app.models.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pruebas.app.models.dao.IFacturaDao;
import com.pruebas.app.models.entity.Factura;

@Service
public class FacturaServiceImpl implements IFacturaService {

	@Autowired
	public IFacturaDao facturaDao;

	@Override
	@Transactional
	public void save(Factura factura) {
		this.facturaDao.save(factura);
	}
}
