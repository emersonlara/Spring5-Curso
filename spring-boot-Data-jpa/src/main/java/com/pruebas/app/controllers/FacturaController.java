package com.pruebas.app.controllers;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pruebas.app.models.entity.Cliente;
import com.pruebas.app.models.entity.Factura;
import com.pruebas.app.models.entity.ItemFactura;
import com.pruebas.app.models.entity.Producto;
import com.pruebas.app.models.service.IClienteService;
import com.pruebas.app.models.service.IFacturaService;
import com.pruebas.app.models.service.IProductoService;

@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {

	@Autowired
	private IClienteService clienteService;
	@Autowired
	private IProductoService productoService;
	@Autowired
	private IFacturaService facturaService;

	private final Logger log = LoggerFactory.getLogger(getClass());

	@GetMapping("/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {
		String path = "";
		Factura factura = this.facturaService.fetchByIdWithClienteWithItemFacturaWithProducto(id);

		if (factura == null) {
			flash.addFlashAttribute("error", "La factura no existe en la base de datos.");
			path = "redirect:/listar";
		} else {
			model.addAttribute("factura", factura);
			model.addAttribute("titulo", "Factura: ".concat(factura.getDescripcion()));
			path = "factura/ver";
		}

		return path;
	}

	@GetMapping("/form/{clienteId}")
	public String crear(@PathVariable(value = "clienteId") Long clienteId, Map<String, Object> model,
			RedirectAttributes flash) {
		Cliente cliente = this.clienteService.findOne(clienteId);
		String path = "factura/form";
		if (cliente == null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos.");
			path = "redirect:/listar";
		}

		Factura factura = new Factura();
		factura.setCliente(cliente);

		model.put("factura", factura);
		model.put("titulo", "Crear Factura");

		return path;
	}

	@GetMapping(value = "/cargar-productos/{term}", produces = { "application/json" })
	public @ResponseBody List<Producto> cargarProductos(@PathVariable String term) {
		return this.productoService.findByNombre(term);
	}

	@PostMapping("/form")
	public String guardar(@Valid Factura factura, BindingResult result, Model model,
			@RequestParam(name = "item_id[]", required = false) Long[] itemId,
			@RequestParam(name = "cantidad[]", required = false) Integer[] cantidad, RedirectAttributes flash,
			SessionStatus status) {
		String path = "";

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Crear factura");
			path = "factura/form";
		} else {

			if (itemId == null || itemId.length == 0) {
				model.addAttribute("titulo", "Crear Factura");
				model.addAttribute("error", "Error: La factura NO puede no tener líneas");
				path = "factura/form";
			} else {

				for (int i = 0; i < itemId.length; i++) {
					// FIXME El producto puede ser nulo porque el id no exita en la bd, no se está
					// controlando
					Producto producto = this.productoService.findById(itemId[i]);

					ItemFactura linea = new ItemFactura();
					linea.setCantidad(cantidad[i]);
					linea.setProducto(producto);

					factura.addItemFactura(linea);

					log.info("ID: " + itemId[i].toString() + ", cantidad: " + cantidad[i].toString());
				}

				this.facturaService.save(factura);

				// Eliminamos de la session el estatus de la factura
				status.setComplete();
				// Enviamos mensaje de éxito
				flash.addFlashAttribute("success", "Factura creada con éxito");

				path = "redirect:/ver/" + factura.getCliente().getId();
			}
		}
		return path;
	}

	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		String path = "";
		Factura factura = this.facturaService.findById(id);

		if (factura != null) {
			this.facturaService.deleteFactura(id);
			flash.addFlashAttribute("success", "Factura eliminada con éxito!");
			path = "redirect:/ver/" + factura.getCliente().getId();
		} else {

			flash.addFlashAttribute("error", "La factura no existe en la base de datos, no se pudo eliminar!");
			path = "redirect:/listar";
		}

		return path;
	}

}
