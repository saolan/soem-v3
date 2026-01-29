package ec.com.tecnointel.soem.serWebSri.registroImpl;

import java.io.Serializable;

import ec.com.tecnointel.soem.serWebClientSri.autorizacion.RespuestaComprobante;
import ec.com.tecnointel.soem.serWebClientSri.general.AutorizacionComprobantesUtil;
import ec.com.tecnointel.soem.serWebClientSri.general.AutorizacionComprobantesWs;
import ec.com.tecnointel.soem.serWebClientSri.general.AutorizacionDTO;
import ec.com.tecnointel.soem.serWebSri.registroInt.AutorizacionComprobantesWsInt;
import jakarta.ejb.Stateless;

@Stateless
public class AutorizacionComprobantesImpWs implements AutorizacionComprobantesWsInt, Serializable {

	private static final long serialVersionUID = -7105591308924058363L;

	@Override
	public RespuestaComprobante autorizarComprobante(String proxyIpUrl, String puerto, String ambiente,
			String urlProduccion, String urlPruebas, String nombreServicio, String claveAcce) throws Exception {

		return AutorizacionComprobantesWs.autorizarComprobante(proxyIpUrl, puerto, ambiente, urlProduccion, urlPruebas,
				nombreServicio, claveAcce);
	}

	@Override
	public AutorizacionDTO obtenerEstadoAutorizacion(RespuestaComprobante respuestaComprobante) throws Exception {

		AutorizacionComprobantesUtil autorizacionComprobantesUtil = new AutorizacionComprobantesUtil(
				respuestaComprobante);

		return autorizacionComprobantesUtil.obtenerEstadoAutorizacion();

	}
	
	@Override
	public void validarRespuestaAutorizacion(AutorizacionDTO autorizacionDTO, String nombreArchivo, String rutaAutorizados) throws Exception {
		
		AutorizacionComprobantesUtil autorizacionComprobantesUtil = new AutorizacionComprobantesUtil(nombreArchivo);
		
		autorizacionComprobantesUtil.validarRespuestaAutorizacion(autorizacionDTO, rutaAutorizados);
		
	}

	// Metodo creado para descargar xml y cargar la compra con la clave de acceso
	@Override
	public void crearArchivoXml(AutorizacionDTO autorizacionDTO, String nombreArchivo, String rutaDescargados) throws Exception {
		
		AutorizacionComprobantesUtil autorizacionComprobantesUtil = new AutorizacionComprobantesUtil(nombreArchivo);
		
		autorizacionComprobantesUtil.crearArchivoXml(autorizacionDTO, rutaDescargados);
		
	}
}
