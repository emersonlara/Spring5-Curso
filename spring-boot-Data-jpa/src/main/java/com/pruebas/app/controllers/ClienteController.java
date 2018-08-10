package com.pruebas.app.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.pruebas.app.models.entity.Cliente;
import com.pruebas.app.models.service.IClienteService;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

	@Autowired
	// @Qualifier("clienteDaoJPA") // Para seleccionar la interfaz a usar, por
	// ejemplo si usamos 2 conectores
	private IClienteService clienteService;

	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public String listar(Model model) {
		model.addAttribute("titulo", "Listado de clientes");
		model.addAttribute("clientes", clienteService.findAll());
		return "listar";
	}

	@GetMapping(value = "/form")
	public String crear(Map<String, Object> model) {
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Formulario de cliente");
		return "form";
	}

	@RequestMapping(value = "/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model) {
		Cliente cliente = new Cliente();
		String res;

		if (id > 0) {
			cliente = clienteService.findOne(id);
			res = "form";
		} else
			res = "redirect:/listar";

		model.put("cliente", cliente);
		model.put("titulo", "Editar cliente");
		return res;
	}

	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model, SessionStatus status) {
		String res;

		if (result.hasErrors()) {
			model.addAttribute(cliente);
			res = "form";
		} else {
			clienteService.save(cliente);
			// Eliminamos la sesión para no dejar el cliente cargado
			status.setComplete();
			res = "redirect:listar";
		}
		return res;
	}
	
	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id) {

		if (id > 0) {
			clienteService.delete(id);
		}

		return "redirect:/listar";
	}


}
