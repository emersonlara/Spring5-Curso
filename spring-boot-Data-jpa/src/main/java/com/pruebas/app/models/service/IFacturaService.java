package com.pruebas.app.models.service;

import com.pruebas.app.models.entity.Factura;

public interface IFacturaService {

	public void save(Factura factura);

	public Factura findById(Long id);

	public void deleteFactura(Long id);

	public Factura fetchByIdWithClienteWithItemFacturaWithProducto(Long id);

}
