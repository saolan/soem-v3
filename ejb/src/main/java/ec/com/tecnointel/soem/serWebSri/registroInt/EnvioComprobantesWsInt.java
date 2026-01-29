package ec.com.tecnointel.soem.serWebSri.registroInt;

import java.io.File;

import ec.com.tecnointel.soem.serWebClientSri.recepcion.RespuestaSolicitud;
import jakarta.ejb.Local;

@Local
public interface EnvioComprobantesWsInt {

	public RespuestaSolicitud obtenerRespuestaEnvio1(File archivo, String ruc, String tipoComprobante,
			String claveDeAcceso, String urlWsdl) throws Exception;

	public String devuelveUrlWs(String proxyIpUrl, String puerto, String ambiente, String urlProduccion,
			String urlPruebas, String nombreServicio) throws Exception;

	public Boolean validarUrl(String proxyIpUrl, String puerto, String ambiente, String urlProduccion,
			String urlPruebas, String nombreServicio, String rutaCertificadoSerWebSri) throws Exception;

	public Boolean existeConexion(String proxyIpUrl, String puerto, String ambiente, String urlProduccion,
			String urlPruebas, String nombreServicio, String rutaCertificadoSerWebSri) throws Exception;

	public String obtenerMensajeRespuesta(RespuestaSolicitud respuestaSolicitud) throws Exception;

}
