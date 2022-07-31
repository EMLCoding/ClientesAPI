package com.emlcoding.springboot.backend.apirest.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.emlcoding.springboot.backend.apirest.models.entity.CustomError;
import com.emlcoding.springboot.backend.apirest.models.entity.Factura;
import com.emlcoding.springboot.backend.apirest.models.entity.Producto;
import com.emlcoding.springboot.backend.apirest.models.services.IClienteService;

@CrossOrigin(origins= {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class FacturaController {

	@Autowired
	private IClienteService clienteService;
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/facturas/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Factura show(@PathVariable Long id) {
		return clienteService.findFacturaById(id);
	}
	
	@Secured({"ROLE_ADMIN"})
	@PostMapping("/facturas")
	public ResponseEntity<?> crear(@RequestBody Factura factura) {
		Factura facturaCreada = null;
		
		try {
			facturaCreada = clienteService.saveFactura(factura);
		} catch(DataAccessException e) {
			CustomError error = new CustomError();
			error.setError(true);
			error.setReason("Se ha producido un error en la petición.");
			return new ResponseEntity<CustomError>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Factura>(facturaCreada, HttpStatus.OK);
	}
	
	@Secured({"ROLE_ADMIN"})
	@DeleteMapping("/facturas/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		try {
			clienteService.deleteFacturaById(id);
		} catch(DataAccessException e) {
			CustomError error = new CustomError();
			error.setError(true);
			error.setReason("Se ha producido un error en la petición.");
			return new ResponseEntity<CustomError>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<String>("Se ha eliminado la factura correctamente", HttpStatus.OK);
		
	}
	
	@Secured({"ROLE_ADMIN"})
	@RequestMapping(method = RequestMethod.GET ,value = {"/productos/filtrar-productos", "/productos/filtrar-productos/{term}"})
	@ResponseStatus(HttpStatus.OK)
	public List<Producto> filtrarProductos(@PathVariable(required=false) String term) {
		if (term != null) {
			return clienteService.findProductoByNombre(term);
		} else {
			return clienteService.findAllProductos();
		}
		
	}
}
