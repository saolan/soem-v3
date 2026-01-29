package ec.com.tecnointel.soem.serWebSri.registroInt;

import ec.com.tecnointel.soem.serWebClientSri.autorizacion.RespuestaComprobante;
import ec.com.tecnointel.soem.serWebClientSri.general.AutorizacionDTO;
import jakarta.ejb.Local;

@Local
public interface AutorizacionComprobantesWsInt {

	public RespuestaComprobante autorizarComprobante(String proxyIpUrl, String puerto, String ambiente, String urlProduccion,
			String urlPruebas, String nombreServicio, String claveAcce) throws Exception;

	public AutorizacionDTO obtenerEstadoAutorizacion(RespuestaComprobante respuestaComprobante) throws Exception;

	public void validarRespuestaAutorizacion(AutorizacionDTO autorizacionDTO, String nombreArchivo, String rutaAutorizados) throws Exception;

	// Metodo creado para descargar xml y cargar la compra con la clalve de acceso 
	public void crearArchivoXml(AutorizacionDTO autorizacionDTO, String nombreArchivo, String rutaAutorizados) throws Exception;
	
}
