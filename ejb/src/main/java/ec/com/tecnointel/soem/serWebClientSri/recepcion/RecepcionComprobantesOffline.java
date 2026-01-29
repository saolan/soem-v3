package ec.com.tecnointel.soem.serWebClientSri.recepcion;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.ws.RequestWrapper;
import jakarta.xml.ws.ResponseWrapper;

@WebService(name = "RecepcionComprobantesOffline", targetNamespace = "http://ec.gob.sri.ws.recepcion")
@XmlSeeAlso({ ObjectFactory.class })
public interface RecepcionComprobantesOffline {

	@WebMethod
	@WebResult(name = "RespuestaRecepcionComprobante", targetNamespace = "")
	@RequestWrapper(localName = "validarComprobante", targetNamespace = "http://ec.gob.sri.ws.recepcion", className = "ec.gob.sri.comprobantes.ws.ValidarComprobante")
	@ResponseWrapper(localName = "validarComprobanteResponse", targetNamespace = "http://ec.gob.sri.ws.recepcion", className = "ec.gob.sri.comprobantes.ws.ValidarComprobanteResponse")
	public abstract RespuestaSolicitud validarComprobante(
			@WebParam(name = "xml", targetNamespace = "") byte[] paramArrayOfByte);

}
