package com.pruebas.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.pruebas.app.models.entity.Factura;

public interface IFacturaDao extends CrudRepository<Factura, Long> {

}
