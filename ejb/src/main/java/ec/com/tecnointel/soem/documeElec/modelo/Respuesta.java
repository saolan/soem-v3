package ec.com.tecnointel.soem.documeElec.modelo;

import java.time.LocalDate;

public class Respuesta {
	private Integer codigo;
	private String claveDeAcceso;
	private String archivo;
	private String estado;
	private LocalDate fecha;

	public Respuesta() {
	}

	public Respuesta(Integer codigo, String claveDeAcceso, String archivo, String estado, LocalDate fecha) {
		
		this.codigo = codigo;
		this.claveDeAcceso = claveDeAcceso;
		this.archivo = archivo;
		this.estado = estado;
		this.fecha = fecha;
	}

	public String getArchivo() {
		return this.archivo;
	}

	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}

	public String getClaveDeAcceso() {
		return this.claveDeAcceso;
	}

	public void setClaveDeAcceso(String claveDeAcceso) {
		this.claveDeAcceso = claveDeAcceso;
	}

	public Integer getCodigo() {
		return this.codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

//	public Date getFecha() {
//		return this.fecha;
//	}
//
//	public void setFecha(Date fecha) {
//		this.fecha = fecha;
//	}
}
