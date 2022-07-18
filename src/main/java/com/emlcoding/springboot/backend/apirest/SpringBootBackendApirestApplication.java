package com.emlcoding.springboot.backend.apirest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// CommandLineRunner permite lanzar un metodo automaticamente al arrancar el servidor
@SpringBootApplication
public class SpringBootBackendApirestApplication implements CommandLineRunner{
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootBackendApirestApplication.class, args);
	}

	// Metodo que se ejecuta automaticamente al arrancar el servidor
	@Override
	public void run(String... args) throws Exception {
		// Esto se ha hecho para ver que contraseña poner a los usuarios que se han añadido directamente en la bbdd a mano
		String password = "12345";
		
		for (int i = 0; i < 4; i++) {
			String pass = passwordEncoder.encode(password);
			System.out.println(pass);
		}
		
	}
	
	

}
