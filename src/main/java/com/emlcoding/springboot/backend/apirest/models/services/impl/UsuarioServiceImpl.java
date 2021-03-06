package com.emlcoding.springboot.backend.apirest.models.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emlcoding.springboot.backend.apirest.models.dao.IUsuarioDao;
import com.emlcoding.springboot.backend.apirest.models.entity.Usuario;
import com.emlcoding.springboot.backend.apirest.models.services.IUsuarioService;

@Service
public class UsuarioServiceImpl implements IUsuarioService, UserDetailsService{
	
	private Logger logger = org.slf4j.LoggerFactory.getLogger(UsuarioServiceImpl.class);

	@Autowired
	private IUsuarioDao usuarioDao;
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Usuario usuario = usuarioDao.findByUsername(username);
		
		if(usuario == null) {
			logger.error("Error en el login: no existe el usuario '" + username + "' en el sistema");
			throw new UsernameNotFoundException("Error en el login: no existe el usuario '" + username + "' en el sistema");
		}
		
		// Es necesario recoger los roles de esta forma para que funcione la securizacion (@Secured) de las llamadas a los metodos del controller
		List<GrantedAuthority> authorities = usuario.getRoles()
				.stream()
				.map(rol -> new SimpleGrantedAuthority(rol.getNombre()))
				.peek(authority -> logger.info("Rol: " + authority.getAuthority()))
				.collect(Collectors.toList());
		
		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
	}

	@Override
	@Transactional(readOnly = true)
	public Usuario findByUsername(String username) {
		return usuarioDao.findByUsername(username);
	}

}
