package com.pruebas.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.pruebas.app.models.entity.Cliente;

public interface IClienteDao extends CrudRepository<Cliente, Long> {
	
}
