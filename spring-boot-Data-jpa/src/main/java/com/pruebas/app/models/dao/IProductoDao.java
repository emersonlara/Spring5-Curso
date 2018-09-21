package com.pruebas.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.pruebas.app.models.entity.Producto;

public interface IProductoDao extends CrudRepository<Producto, Long> {

	@Query("select p from Producto p where p.nombre like %?1%")
	public List<Producto> findByNombre(String term);

	// Podemos ver en la documentación de Spring que si ponemos el nombre del método
	// LikeIgnoreCase no hace falta realizar la consulta
	public List<Producto> findByNombreLikeIgnoreCase(String term);

}
