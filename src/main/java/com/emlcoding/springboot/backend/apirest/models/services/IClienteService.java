package com.emlcoding.springboot.backend.apirest.models.services;

import java.util.List;
import java.util.UUID;

import com.emlcoding.springboot.backend.apirest.models.entity.Cliente;

public interface IClienteService {

	public List<Cliente> findAll();
	
	public Cliente save(Cliente cliente);
	
	public Cliente findById(UUID id);
	
	public void delete(UUID id);
}
