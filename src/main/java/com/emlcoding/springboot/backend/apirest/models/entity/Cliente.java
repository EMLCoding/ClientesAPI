package com.emlcoding.springboot.backend.apirest.models.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="clientes")// El nombre de la tabla en la BBDD
public class Cliente implements Serializable{

	private static final long serialVersionUID = 1L; // Requerido por el protocolo Serializable
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	// @NotEmpty, @Size, @Email son validaciones propias de Spring. Para tener acceso a ellas hay que añadir en el pom.xml la dependencia spring-boot-starter-validation
	@NotEmpty
	@Size(min=4, max=20, message="el tamaño tiene que estar entre 4 y 12")
	private String nombre;
	
	@NotEmpty
	private String apellido;
	
	@NotEmpty
	@Email(message="no es una dirección de correo válida")
	private String email;
	
	// El @NotNull para los campos Date es lo mismo que el @NotEmpty para los demás
	//@NotNull(message = "No puede estar vacio")
	@Column(name = "create_at") // El @Column no es necesario en las demas propiedades porque se van a llamar igual en bbdd que en java
	private LocalDate createAt;
	
	private String foto;
	
	@NotNull(message="es obligatorio seleccionar una región")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="region_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Region region;
	
	// el mappedBy es el nombre de la propiedad en la clase Factura que esta mapeado con esta clase
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cliente", cascade = CascadeType.ALL)
	// El allowSetter es para que no de un bug de recursion infinita al actualizar un cliente por culpa de las facturas. Tambien hay que ponerlo en la propiedad de la clase Factura
	@JsonIgnoreProperties(value={"cliente", "hibernateLazyInitializer", "handler"}, allowSetters = true)// Si no ponemos esto (lo de cliente) en el Json de un cliente se trae las facturas y por cada factura se trae al cliente, y asi formando un bucle infinito
	private List<Factura> facturas;
	
	public Cliente() {
		this.facturas = new ArrayList<>();
	}

	// Esto se ejecuta automaticamente cuando se vaya a insertar un nuevo cliente en la bbdd
	@PrePersist
	public void prePersist() throws ParseException {
		createAt = LocalDate.now();
	}
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public LocalDate getCreateAt() {
		return createAt;
	}
	public void setCreateAt(LocalDate createAt) {
		this.createAt = createAt;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public List<Factura> getFacturas() {
		return facturas;
	}

	public void setFacturas(List<Factura> facturas) {
		this.facturas = facturas;
	}
	
	
}
