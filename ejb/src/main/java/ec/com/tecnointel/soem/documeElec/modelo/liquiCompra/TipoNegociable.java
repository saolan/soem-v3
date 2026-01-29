package ec.com.tecnointel.soem.documeElec.modelo.liquiCompra;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tipoNegociable", propOrder = { "correo" })
public class TipoNegociable {

	@XmlElement(required = true)
	protected String correo;

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String value) {
		this.correo = value;
	}
}
