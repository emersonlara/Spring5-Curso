package com.pruebas.app.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pruebas.app.models.entity.Cliente;
import com.pruebas.app.models.service.IClienteService;
import com.pruebas.app.util.paginator.PageRender;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

	// @Qualifier("clienteDaoJPA") // Para seleccionar la interfaz a usar, por
	// ejemplo si usamos 2 conectores
	@Autowired
	private IClienteService clienteService;

	private final Logger log = LoggerFactory.getLogger(ClienteController.class);

	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		String redirect = "";
		Cliente cliente = clienteService.findOne(id);
		if (cliente == null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			redirect = "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Detalle cliente: " + cliente.getNombre());
		redirect = "ver";
		return redirect;
	}

	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") Integer page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 4);

		Page<Cliente> clientes = clienteService.findAll(pageRequest);
		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);

		model.addAttribute("titulo", "Listado de clientes");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
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
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Cliente cliente = new Cliente();
		String res;

		if (id > 0) {
			cliente = clienteService.findOne(id);
			res = "form";
			if (cliente == null) {
				flash.addFlashAttribute("error", "El ID del cliente no existe en la BBDD.");
				res = "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", "El ID del cliente no puede ser cero.");
			res = "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Editar cliente");
		return res;
	}

	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model,
			@RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {
		String res;

		if (result.hasErrors()) {
			model.addAttribute(cliente);
			res = "form";
		} else {
			if (!foto.isEmpty()) {
//				Path directorioRecursos = Paths.get("src/main/resources/static/uploads");
//				String rootPath = directorioRecursos.toFile().getAbsolutePath();
//				String rootPath = "/home/framirez/Documentos/Cursos/Java/Resources/uploads";

				/** es otro método para poner las rutas */
				String uniqueFilename = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename();
				Path rootPath = Paths.get("uploads").resolve(uniqueFilename);
				Path rootAbsolutePath = rootPath.toAbsolutePath();

				log.info("rootPath: " + rootPath);
				log.info("rootAbsolutePath: " + rootAbsolutePath);

				try {
//					byte[] bytes = foto.getBytes();
//					Path rutaCompleta = Paths.get(rootPath, "//", foto.getOriginalFilename());
//					Files.write(rutaCompleta, bytes);
					/** copy sustituye lo anterior */
					Files.copy(foto.getInputStream(), rootAbsolutePath);

					flash.addFlashAttribute("info", "Has subido correctamente '" + uniqueFilename + "'");

					cliente.setFoto(uniqueFilename);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			String mensajeFlash = cliente.getId() != null ? "¡Cliente editado con éxito!"
					: "¡Cliente creado con éxito!";

			clienteService.save(cliente);
			// Eliminamos la sesión para no dejar el cliente cargado
			status.setComplete();
			// Mensaje para el usuario
			flash.addFlashAttribute("success", mensajeFlash);
			res = "redirect:listar";
		}
		return res;
	}

	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		if (id > 0) {
			clienteService.delete(id);
			flash.addFlashAttribute("success", "¡Cliente eliminado con éxito!");
		}

		return "redirect:/listar";
	}

}
