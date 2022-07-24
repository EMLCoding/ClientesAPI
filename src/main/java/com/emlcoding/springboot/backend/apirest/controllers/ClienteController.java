package com.emlcoding.springboot.backend.apirest.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.emlcoding.springboot.backend.apirest.models.entity.Cliente;
import com.emlcoding.springboot.backend.apirest.models.entity.CustomError;
import com.emlcoding.springboot.backend.apirest.models.entity.Region;
import com.emlcoding.springboot.backend.apirest.models.services.IClienteService;

@CrossOrigin(origins= {"http://localhost:4200"}) // Para permitir el traspaso de recursos entre cliente y servidor. En este caso se permite para por ejemplo una aplicacion con angular como cliente
@RestController
@RequestMapping("/api")
public class ClienteController {
	
	private final Logger log = LoggerFactory.getLogger(ClienteController.class);
	
	@Autowired
	private IClienteService clienteService;

	@GetMapping("/clientes")
	public List<Cliente> getAll() {
		return clienteService.findAll();
	}
	
	@GetMapping("/clientes/page/{page}")
	public Page<Cliente> getAllPagination(@PathVariable Integer page) {
		Pageable pageable = PageRequest.of(page, 2);
		return clienteService.findAll(pageable);
	}
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/cliente/{id}")
	public ResponseEntity<?> getCliente(@PathVariable UUID id) {
		Cliente cliente = null;
		
		try {
			cliente = clienteService.findById(id);
		} catch(DataAccessException e) {
			CustomError error = new CustomError();
			error.setError(true);
			error.setReason("Se ha producido un error en la petición.");
			return new ResponseEntity<CustomError>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (cliente == null) {
			CustomError error = new CustomError();
			error.setError(true);
			error.setReason("No existe un cliente con ese ID.");
			return new ResponseEntity<CustomError>(error, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
	}
	
	// El @Valid es para utilizar las validaciones de la clase Cliente. El objeto BindingResult lleva todos los mensajes de error de la validacion
	@Secured({"ROLE_ADMIN"})
	@PostMapping("/cliente")
	public ResponseEntity<?> createCliente(@Valid @RequestBody Cliente cliente, BindingResult result) {
		Cliente clienteCreado = null;
		
		if (result.hasErrors()) {
			StringBuilder errorBuilder = new StringBuilder();
			result.getFieldErrors().forEach(err -> errorBuilder.append("El campo '" + err.getField() + "' " + err.getDefaultMessage() + ". "));
			
			CustomError error = new CustomError();
			error.setError(true);
			error.setReason(errorBuilder.toString());
			return new ResponseEntity<CustomError>(error, HttpStatus.BAD_REQUEST);
		}
		
		try {
			clienteCreado = clienteService.save(cliente);
		} catch(DataAccessException e) {
			CustomError error = new CustomError();
			error.setError(true);
			error.setReason("Se ha producido un error en la petición.");
			return new ResponseEntity<CustomError>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Cliente>(clienteCreado, HttpStatus.OK);
	}
	
	@Secured({"ROLE_ADMIN"})
	@PutMapping("/cliente/{id}")
	public ResponseEntity<?> update(@RequestBody Cliente cliente, @PathVariable UUID id) {
		
		Cliente clienteActual = clienteService.findById(id);
		
		Cliente clienteActualizado = null;
		
		if (clienteActual == null) {
			CustomError error = new CustomError();
			error.setError(true);
			error.setReason("No existe un cliente con ese ID.");
			return new ResponseEntity<CustomError>(error, HttpStatus.NOT_FOUND);
		}
		
		try {
			clienteActual.setApellido(cliente.getApellido());
			clienteActual.setNombre(cliente.getNombre());
			clienteActual.setEmail(cliente.getEmail());
			clienteActual.setRegion(cliente.getRegion());
			
			clienteActualizado = clienteService.save(clienteActual);
		} catch (DataAccessException e) {
			CustomError error = new CustomError();
			error.setError(true);
			error.setReason("Se ha producido un error en la petición.");
			return new ResponseEntity<CustomError>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// Como el cliente tiene un ID entonces el .save hace un update en la bbdd
		return new ResponseEntity<Cliente>(clienteActualizado, HttpStatus.OK);
	}
	
	@Secured({"ROLE_ADMIN"})
	@DeleteMapping("/cliente/{id}")
	public ResponseEntity<?> deletecliente(@PathVariable UUID id) {
		try {
			clienteService.delete(id);
		} catch(DataAccessException e) {
			CustomError error = new CustomError();
			error.setError(true);
			error.setReason("Se ha producido un error en la petición.");
			return new ResponseEntity<CustomError>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<String>("Se ha eliminado el cliente correctamente", HttpStatus.OK);
	}
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@PostMapping("/clientes/upload")
	public ResponseEntity<?> uploadImage(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") UUID id) {
		
		Cliente cliente = clienteService.findById(id);
		
		if(!archivo.isEmpty()) {
			String nombreArchivo = id + "-profile" + getExtension(archivo.getOriginalFilename());
			Path rutaArchivo = Paths.get("uploads").resolve(nombreArchivo).toAbsolutePath();
			
			log.info("Archivo que se va a subir: {}", rutaArchivo.toString());
			
			try {
				if (Files.exists(rutaArchivo)) {
					Files.delete(rutaArchivo);
				}
				Files.copy(archivo.getInputStream(), rutaArchivo);
				
			} catch (IOException e) {
				CustomError error = new CustomError();
				error.setError(true);
				error.setReason("Se ha producido un error al subir la imagen.");
				return new ResponseEntity<CustomError>(error, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			cliente.setFoto(nombreArchivo);
			clienteService.save(cliente);
		}
		
		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
	}
	
	// El ':.+' es para indicar que nombreFoto va a llevar la extension. Es una expresion regular
	@GetMapping("/uploads/img/{nombreFoto:.+}")
	public ResponseEntity<Resource> getFoto(@PathVariable String nombreFoto) {
		Path rutaArchivo = Paths.get("uploads").resolve(nombreFoto).toAbsolutePath();
		Resource recurso = null;
		
		try {
			recurso = new UrlResource(rutaArchivo.toUri());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		
		if(!recurso.exists() || !recurso.isReadable()) {
			throw new RuntimeException("Error: no se pudo cargar la imagen");
		}
		HttpHeaders cabecera = new HttpHeaders();
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"");
		
		return new ResponseEntity<Resource>(recurso, cabecera, HttpStatus.OK);
	}
	
	@Secured({"ROLE_ADMIN"})
	@GetMapping("/clientes/regiones")
	public List<Region> getAllRegiones() {
		return clienteService.findAllRegiones();
	}
	
	private String getExtension(String nombreArchivo) {
		String extension = "";
		
		extension = nombreArchivo.substring(nombreArchivo.lastIndexOf("."), nombreArchivo.length());
		
		
		return extension;
	}
}
