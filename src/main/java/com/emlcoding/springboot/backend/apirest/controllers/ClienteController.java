package com.emlcoding.springboot.backend.apirest.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.emlcoding.springboot.backend.apirest.models.entity.Cliente;
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
	public Cliente getCliente(@PathVariable UUID id) {
		return clienteService.findById(id);
	}
	
	@PostMapping("/cliente")
	@ResponseStatus(HttpStatus.CREATED)
	public Cliente createCliente(@RequestBody Cliente cliente) {
		return clienteService.save(cliente);
	}
	
	@DeleteMapping("/cliente")
	public void deletecliente(@PathVariable UUID id) {
		clienteService.delete(id);
	}
}