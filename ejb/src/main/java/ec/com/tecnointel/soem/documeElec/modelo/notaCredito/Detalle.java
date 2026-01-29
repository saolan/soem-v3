package ec.com.tecnointel.soem.documeElec.modelo.notaCredito;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "detalle", propOrder = { "motivoModificacion" })
public class Detalle {
	@XmlElement(required = true)
	protected String motivoModificacion;

	public String getMotivoModificacion() {
		return this.motivoModificacion;
	}

	public void setMotivoModificacion(String value) {
		this.motivoModificacion = value;
	}
}