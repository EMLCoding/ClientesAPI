package com.emlcoding.springboot.backend.apirest.models.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.emlcoding.springboot.backend.apirest.models.entity.Cliente;

// El Long es por el tipo de dato ID de la clase Cliente
public interface IClienteDao extends CrudRepository<Cliente, Long>{

	@Query("SELECT c FROM Cliente c WHERE c.id = ?1")
	Cliente findByUUID(UUID id);
	
	@Modifying
	@Query("DELETE FROM Cliente c WHERE id = :id")
	void deleteCustom(UUID id);
}
