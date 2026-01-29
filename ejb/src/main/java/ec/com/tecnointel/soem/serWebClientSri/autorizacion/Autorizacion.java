package ec.com.tecnointel.soem.serWebClientSri.autorizacion;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import ec.com.tecnointel.soem.serWebClientSri.general.CDATAAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

//Aumentado anotacion version 2.0
@XmlRootElement(name = "autorizacion")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "autorizacion", propOrder = { "estado", "numeroAutorizacion", "fechaAutorizacion", "ambiente", "comprobante",
		"mensajes" })
public class Autorizacion {
	
	protected String estado;
	protected String numeroAutorizacion;
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar fechaAutorizacion;
	protected String ambiente;
	//Aumentado anotacion y CDATAAdapter.class para aumentar CDATA version 2.0
	@XmlJavaTypeAdapter(value = CDATAAdapter.class)
	protected String comprobante;
	protected Mensajes mensajes;

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String value) {
		this.estado = value;
	}

	public String getNumeroAutorizacion() {
		return this.numeroAutorizacion;
	}

	public void setNumeroAutorizacion(String value) {
		this.numeroAutorizacion = value;
	}

	public XMLGregorianCalendar getFechaAutorizacion() {
		return this.fechaAutorizacion;
	}

	public void setFechaAutorizacion(XMLGregorianCalendar value) {
		this.fechaAutorizacion = value;
	}

	public String getAmbiente() {
		return ambiente;
	}

	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}

	public String getComprobante() {
		return this.comprobante;
	}

	public void setComprobante(String value) {
		this.comprobante = value;
	}

	public Mensajes getMensajes() {
		return this.mensajes;
	}

	public void setMensajes(Mensajes value) {
		this.mensajes = value;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "mensaje" })
	public static class Mensajes {
		protected List<Mensaje> mensaje;

		public List<Mensaje> getMensaje() {
			if (this.mensaje == null) {
				this.mensaje = new ArrayList();
			}
			return this.mensaje;
		}
	}
}
