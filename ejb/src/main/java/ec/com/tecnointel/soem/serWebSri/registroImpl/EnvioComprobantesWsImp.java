package ec.com.tecnointel.soem.serWebSri.registroImpl;

import java.io.File;
import java.io.Serializable;

import ec.com.tecnointel.soem.serWebClientSri.general.EnvioComprobantesWs;
import ec.com.tecnointel.soem.serWebClientSri.general.SerWebUtil;
import ec.com.tecnointel.soem.serWebClientSri.recepcion.RespuestaSolicitud;
import ec.com.tecnointel.soem.serWebSri.registroInt.EnvioComprobantesWsInt;
import jakarta.ejb.Stateless;

@Stateless
public class EnvioComprobantesWsImp implements EnvioComprobantesWsInt, Serializable {

	private static final long serialVersionUID = -3262514955168247533L;

	@Override
	public RespuestaSolicitud obtenerRespuestaEnvio1(File archivo, String ruc, String tipoComprobante,
			String claveDeAcceso, String urlWsdl) throws Exception {

		return EnvioComprobantesWs.obtenerRespuestaEnvio1(archivo, ruc, tipoComprobante, claveDeAcceso, urlWsdl);
	}

	@Override
	public String devuelveUrlWs(String proxyIpUrl, String puerto, String ambiente, String urlProduccion,
			String urlPruebas, String nombreServicio) throws Exception {

		return SerWebUtil.devuelveUrlWs(proxyIpUrl, puerto, ambiente, urlProduccion, urlPruebas, nombreServicio);

	}

	@Override
	public Boolean validarUrl(String proxyIpUrl, String puerto, String ambiente, String urlProduccion,
			String urlPruebas, String nombreServicio, String rutaCertificadoSerWebSri) throws Exception {

		return SerWebUtil.validarUrl(proxyIpUrl, puerto, ambiente, urlProduccion, urlPruebas, nombreServicio, rutaCertificadoSerWebSri);
	}

	@Override
	public Boolean existeConexion(String proxyIpUrl, String puerto, String ambiente, String urlProduccion,
			String urlPruebas, String nombreServicio, String rutaCertificadoSerWebSri) throws Exception {

		return SerWebUtil.existeConexion(proxyIpUrl, puerto, ambiente, urlProduccion, urlPruebas, nombreServicio, rutaCertificadoSerWebSri);
	}
	
	@Override
	public String obtenerMensajeRespuesta(RespuestaSolicitud respuestaSolicitud) throws Exception {
		return EnvioComprobantesWs.obtenerMensajeRespuesta(respuestaSolicitud); 
	}
}
