package ec.com.tecnointel.soem.serWebClientSri.recepcion;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "validarComprobanteResponse", propOrder = { "respuestaRecepcionComprobante" })
public class ValidarComprobanteResponse {

	@XmlElement(name = "RespuestaRecepcionComprobante")
	protected RespuestaSolicitud respuestaRecepcionComprobante;

	public RespuestaSolicitud getRespuestaRecepcionComprobante() {
		return this.respuestaRecepcionComprobante;
	}

	public void setRespuestaRecepcionComprobante(RespuestaSolicitud value) {
		this.respuestaRecepcionComprobante = value;
	}

}
