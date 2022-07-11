package com.emlcoding.springboot.backend.apirest.models.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emlcoding.springboot.backend.apirest.models.dao.IClienteDao;
import com.emlcoding.springboot.backend.apirest.models.entity.Cliente;
import com.emlcoding.springboot.backend.apirest.models.services.IClienteService;

@Service
public class ClienteServiceImpl implements IClienteService{
	@Autowired
	private IClienteDao clienteDao;

	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		return (List<Cliente>) clienteDao.findAll(); // El findAll es un metodo ya incluido en el CrudRepository
	}

	@Override
	@Transactional
	public Cliente save(Cliente cliente) {
		return clienteDao.save(cliente);
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente findById(UUID id) {
		return clienteDao.findByUUID(id);
	}

	@Override
	@Transactional
	public void delete(UUID id) {
		clienteDao.deleteCustom(id);
	}

}
