package ec.com.tecnointel.soem.serWebClientSri.autorizacion;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "autorizacionComprobanteLote", propOrder = { "claveAccesoLote" })
public class AutorizacionComprobanteLote {
	
	protected String claveAccesoLote;

	public String getClaveAccesoLote() {
		return this.claveAccesoLote;
	}

	public void setClaveAccesoLote(String value) {
		this.claveAccesoLote = value;
	}

}
