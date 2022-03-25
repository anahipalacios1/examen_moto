package com.pruebas.springboot.di.app.controller;

import java.sql.SQLException;
import java.util.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pruebas.springboot.di.app.models.Proveedor;

import jakarta.validation.Valid;
@Controller
@RequestMapping("/proveedores")
public class ProveedorController {

	@GetMapping("/listar")
	public String listaProv(Model model) {
		ProveedorManager proveedorManager = new ProveedorManager();
		
		List<Proveedor> proveedores = new ArrayList<>();
		proveedores = proveedorManager.getAllProveedores();
		model.addAttribute("titulo", "Lista de Proveedores");
		model.addAttribute("mensaje", "Lista de Proveedores");
		model.addAttribute("idproveedor", "Idproveedor");
		model.addAttribute("nombre", "Nombre");
		model.addAttribute("proveedores", proveedores);
		return "proveedor-template/listarProveedores"; 
	}
	
	@GetMapping("/agregar")
	public String agregarProv(Model model) {
		Proveedor proveedor = new Proveedor();
		model.addAttribute("titulo", "Agregar proveedor");
		model.addAttribute("proveedor", proveedor);
		model.addAttribute("error", new HashMap<>());
		return "proveedor-template/agregar";
	}
	
	@PostMapping("/agregar")
	public String agregarProveedor(@Valid Proveedor proveedor, BindingResult result, Model model,
			@RequestParam(name = "nombre") String nombre)
		throws SQLException {
			model.addAttribute("titulo", "Falta datos");
		if (result.hasErrors()) {
			Map<String, String> errores = new HashMap<>();
			result.getFieldErrors().forEach(err -> {
				errores.put(err.getField(),
						"El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
			});
			model.addAttribute("error", errores);
			return "proveedor-template/agregar";
		}
		
		ProveedorManager ProveedorManager = new ProveedorManager();
		proveedor = ProveedorManager.add(nombre);  
		model.addAttribute("idproveedor", "Idproveedor");
		model.addAttribute("nombre", "Nombre");
		model.addAttribute("titulo", "Proveedor Agregado");
		model.addAttribute("proveedor", proveedor);
		return "proveedor-template/resultadoProv";
	}

	@GetMapping("/modificar")
	public String modificar(Model model) {
		Proveedor proveedor = new Proveedor();
		model.addAttribute("titulo", "Modificar Proveedor");
		model.addAttribute("proveedor", proveedor);
		model.addAttribute("error", new HashMap<>());
		return "proveedor-template/modificar";
	}

	@PostMapping("/modificar")
	public String modificar(@Valid Proveedor proveedor, BindingResult result, Model model,
			@RequestParam(name = "idproveedor") int idproveedor, @RequestParam(name = "nombre") String nombre) {
		model.addAttribute("titulo", "Falta datos");
		if (result.hasErrors()) {
			Map<String, String> errores = new HashMap<>();
			result.getFieldErrors().forEach(err -> {
				errores.put(err.getField(),
						"El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
			});
			model.addAttribute("titulo", "Modificar Proveedor");
			model.addAttribute("error", errores);
			return "proveedor-template/modificar";
		}
		ProveedorManager proveedorManager = new ProveedorManager();
		proveedorManager.modify(idproveedor, nombre);
		proveedor.setIdproveedor(idproveedor);
		proveedor.setNombre(nombre);
		model.addAttribute("idproveedor", "Idproveedor");
		model.addAttribute("nombre", "Nombre");
		model.addAttribute("proveedor", proveedor);
		return "proveedor-template/resultadoProv";
	}

	@GetMapping("/buscar")
	public String buscarProv(Model model) {
		Proveedor proveedor = new Proveedor();
		model.addAttribute("titulo", "Buscar proveedor");
		model.addAttribute("proveedor", proveedor);
		model.addAttribute("error", new HashMap<>());
		return "proveedor-template/buscar";
	}

	@PostMapping("/buscar")
	public String buscarProv(@Valid Proveedor proveedor, BindingResult result, Model model,
			@RequestParam(name = "idproveedor") int idproveedor) throws SQLException {

		if (result.hasGlobalErrors()) {
			Map<String, String> errores = new HashMap<>();
			result.getFieldErrors().forEach(err -> {
				errores.put(err.getField(),
						"El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
			});
			model.addAttribute("titulo", "Debe ser numero entero");
			model.addAttribute("error", errores);
			return "proveedor-template/buscar";
		} /*Se llama al metodo de getByid de la clase Manager y se manda a la vista*/
		ProveedorManager proveedorManager = new ProveedorManager();
		proveedor = proveedorManager.getByid(idproveedor);
		model.addAttribute("idproveedor", "Idproveedor");
		model.addAttribute("nombre", "Nombre");
		model.addAttribute("titulo", "Proveedor Encontrado");
		model.addAttribute("proveedor", proveedor);
		return "proveedor-template/resultadoProv";

	}

	@GetMapping("/eliminar")
	public String eliminarAlu(Model model) {
		Proveedor proveedor1 = new Proveedor();
		model.addAttribute("titulo", "Eliminar Proveedor");
		model.addAttribute("proveedor", proveedor1);
		model.addAttribute("error", new HashMap<>());
		return "proveedor-template/eliminar";
	}

	@PostMapping("/eliminar")
	public String eliminarProv(@Valid Proveedor proveedor1, BindingResult result, Model model,
			@RequestParam(name = "idproveedor") int idproveedor) throws SQLException {

		if (result.hasGlobalErrors()) {
			Map<String, String> errores = new HashMap<>();
			result.getFieldErrors().forEach(err -> {
				errores.put(err.getField(),
						"El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
			});
			model.addAttribute("titulo", "Debe ser numero entero");
			model.addAttribute("error", errores);
			return "proveedor-template/eliminar";
		}
		ProveedorManager proveedorManager = new ProveedorManager();
		proveedor1 = proveedorManager.getByid(idproveedor);
		if (proveedorManager.delete(idproveedor) == false) {
			model.addAttribute("idproveedor", " ");
			model.addAttribute("nombre", " ");
			model.addAttribute("titulo", "El proveedor no se puede eliminar esta referido a otra base de datos");
			model.addAttribute("proveedor", proveedor1);

		} else {
			if (proveedor1 == null) {
				model.addAttribute("idproveedor", " ");
				model.addAttribute("nombre", " ");
				model.addAttribute("titulo", "El proveedor no se encuentra en la base de datos");
				model.addAttribute("proveedor", proveedor1);
			} else {
				model.addAttribute("idproveedor", "Idproveedor");
				model.addAttribute("nombre", "Nombre");
				model.addAttribute("titulo", "Proveedor Eliminado");
				model.addAttribute("proveedor", proveedor1);
			}
		}

		return "proveedor-template/resultadoEliminado";

	}//main

}
