package com.emlcoding.springboot.backend.apirest.models.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.emlcoding.springboot.backend.apirest.models.entity.Cliente;
import com.emlcoding.springboot.backend.apirest.models.entity.Region;

// El Long es por el tipo de dato ID de la clase Cliente
// JpaRespository hereda de CrudRepository. Se va a utilizar para tener paginacion
public interface IClienteDao extends JpaRepository<Cliente, Long>{

	@Query("SELECT c FROM Cliente c WHERE c.id = ?1")
	Cliente findByUUID(UUID id);
	
	@Modifying
	@Query("DELETE FROM Cliente c WHERE id = :id")
	void deleteCustom(UUID id);
	
	@Query("SELECT r FROM Region r")
	public List<Region> findAllRegiones();
}
