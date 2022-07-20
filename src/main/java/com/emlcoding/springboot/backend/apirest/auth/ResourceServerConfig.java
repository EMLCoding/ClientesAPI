package com.emlcoding.springboot.backend.apirest.auth;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

// Se encargara de dar acceso a los recursos a aquellos clientes autenticados, con los mecanismos de OAUTH
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// En este caso se utilizan las configuraciones de seguridad de OAUTH
		// Las restricciones se van poniendo en orden, las de arriba preceden a las de abajo
		// Asi se esta dando permisos a la ruta get de "/api/clientes" a todos los usuarios de la app cliente, y al resto de llamadas solo a los autenticados
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/clientes", "/api/clientes/page/**", "/api/uploads/img/**", "/images/**").permitAll()
		/* SE COMENTA PORQUE SE UTILIZAN LAS @SECURE EN EL CONTROLADOR, PERO SERIA LO MISMO
		.antMatchers(HttpMethod.GET, "/api/clientes/{id}").hasAnyRole("USER", "ADMIN") // hasAnyRole concatena automaticamente 'ROLE_' al string pasado para cada rol
		.antMatchers(HttpMethod.POST, "/api/clientes/upload").hasAnyRole("USER", "ADMIN")
		.antMatchers(HttpMethod.POST, "/api/clientes").hasRole("ADMIN") // hasAnyRole es para añadir varios roles y hasRole para uno solo, pero funcionan igual
		.antMatchers("/api/clientes/**").hasRole("ADMIN") // para todas las rutas /api/clientes/**, independiente si es get, post, put... es solo para usuarios admin
		*/
		.anyRequest().authenticated()
		.and()
		.cors().configurationSource(corsConfigurationSource());
	}
	
	// Metodo para habilitar el CORS entre un front y un back que no se encuentran en el mismo dominio. En este caso es para un proyecto web que se ejecuta en local
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		
		config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		config.setAllowCredentials(true);
		config.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
	
	// Metodo para habilitar la autenticacion, con OAUTH 2, entre un front y un back que no se encuentran en el mismo dominio.
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(corsConfigurationSource()));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}

}
