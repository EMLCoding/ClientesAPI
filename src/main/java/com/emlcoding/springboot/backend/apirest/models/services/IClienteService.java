package com.emlcoding.springboot.backend.apirest.models.services;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.emlcoding.springboot.backend.apirest.models.entity.Cliente;
import com.emlcoding.springboot.backend.apirest.models.entity.Factura;
import com.emlcoding.springboot.backend.apirest.models.entity.Producto;
import com.emlcoding.springboot.backend.apirest.models.entity.Region;

public interface IClienteService {

	public List<Cliente> findAll();
	
	public Page<Cliente> findAll(Pageable pageable);
	
	public Cliente save(Cliente cliente);
	
	public Cliente findById(UUID id);
	
	public void delete(UUID id);
	
	public List<Region> findAllRegiones();
	
	public Factura findFacturaById(Long id);
	
	public Factura saveFactura(Factura factura);
	
	public void deleteFacturaById(Long id);
	
	public List<Producto> findProductoByNombre(String term);
	
	public List<Producto> findAllProductos();
}
