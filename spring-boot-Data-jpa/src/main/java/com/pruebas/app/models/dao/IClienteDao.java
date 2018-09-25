package com.pruebas.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.pruebas.app.models.entity.Cliente;

public interface IClienteDao extends PagingAndSortingRepository<Cliente, Long> {

	// join fetch equivale a un inner join,
	// agregamos el left para que si no tiene facturas el cliente lo muestre de
	// todas formas
	@Query("select c from Cliente c left join fetch c.facturas f where c.id=?1")
	public Cliente fetchByIdWhitFacturas(Long id);

}
