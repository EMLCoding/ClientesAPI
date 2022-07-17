package com.emlcoding.springboot.backend.apirest.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.emlcoding.springboot.backend.apirest.models.entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long>{

	// Esto internamente hace un Select WHere username = parametro pasado
	public Usuario findByUsername(String username);
}
