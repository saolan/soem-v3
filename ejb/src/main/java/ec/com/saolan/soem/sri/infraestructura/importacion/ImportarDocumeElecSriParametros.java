package ec.com.saolan.soem.sri.infraestructura.importacion;

import java.io.Serializable;

import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;

public class ImportarDocumeElecSriParametros implements Serializable {

	private static final long serialVersionUID = 1L;

	private Sucursal sucursal;
	private PersUsua persUsua;
	private String correo;

	public ImportarDocumeElecSriParametros() {
	}

	public ImportarDocumeElecSriParametros(Sucursal sucursal, PersUsua persUsua, String correo) {
		this.sucursal = sucursal;
		this.persUsua = persUsua;
		this.correo = correo;
	}
	
	public Sucursal getSucursal() {
		return sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	public PersUsua getPersUsua() {
		return persUsua;
	}

	public void setPersUsua(PersUsua persUsua) {
		this.persUsua = persUsua;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}
}
