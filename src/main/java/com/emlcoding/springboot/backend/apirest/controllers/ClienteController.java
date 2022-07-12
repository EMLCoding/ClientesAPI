package com.emlcoding.springboot.backend.apirest.controllers;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.emlcoding.springboot.backend.apirest.models.entity.Cliente;
import com.emlcoding.springboot.backend.apirest.models.entity.CustomError;
import com.emlcoding.springboot.backend.apirest.models.services.IClienteService;

@CrossOrigin(origins= {"http://localhost:4200"}) // Para permitir el traspaso de recursos entre cliente y servidor. En este caso se permite para por ejemplo una aplicacion con angular como cliente
@RestController
@RequestMapping("/api")
public class ClienteController {
	
	@Autowired
	private IClienteService clienteService;

	@GetMapping("/clientes")
	public List<Cliente> getAll() {
		return clienteService.findAll();
	}
	
	@GetMapping("/cliente/{id}")
	public ResponseEntity<?> getCliente(@PathVariable UUID id) {
		Cliente cliente = null;
		
		try {
			cliente = clienteService.findById(id);
		} catch(DataAccessException e) {
			CustomError error = new CustomError();
			error.setError(true);
			error.setReason("Se ha producido un error en la petición.");
			return new ResponseEntity<CustomError>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (cliente == null) {
			CustomError error = new CustomError();
			error.setError(true);
			error.setReason("No existe un cliente con ese ID.");
			return new ResponseEntity<CustomError>(error, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
	}
	
	@PostMapping("/cliente")
	@ResponseStatus(HttpStatus.OK)
	public Cliente createCliente(@RequestBody Cliente cliente) {
		return clienteService.save(cliente);
	}
	
	@PutMapping("/cliente/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Cliente update(@RequestBody Cliente cliente, @PathVariable UUID id) {
		
		Cliente clienteActual = clienteService.findById(id);
		
		clienteActual.setApellido(cliente.getApellido());
		clienteActual.setNombre(cliente.getNombre());
		clienteActual.setEmail(cliente.getEmail());
		
		// Como el cliente tiene un ID entonces el .save hace un update en la bbdd
		return clienteService.save(clienteActual);
	}
	
	@DeleteMapping("/cliente/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deletecliente(@PathVariable UUID id) {
		clienteService.delete(id);
	}
}
