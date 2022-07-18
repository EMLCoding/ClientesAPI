package com.emlcoding.springboot.backend.apirest.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

// Se encargara de dar acceso a los recursos a aquellos clientes autenticados, con los mecanismos de OAUTH
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// En este caso se utilizan las configuraciones de seguridad de OAUTH
		// Asi se esta dando permisos a la ruta get de "/api/clientes" a todos los usuarios de la app cliente, y al resto de llamadas solo a los autenticados
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/clientes").permitAll()
		.anyRequest().authenticated();
	}
	
	

}
