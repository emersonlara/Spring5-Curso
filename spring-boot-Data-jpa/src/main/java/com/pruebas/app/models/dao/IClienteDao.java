package com.pruebas.app.models.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.pruebas.app.models.entity.Cliente;

public interface IClienteDao extends PagingAndSortingRepository<Cliente, Long> {
	
}
