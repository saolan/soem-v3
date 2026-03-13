package ec.com.saolan.soem.sri.infraestructura.cliente;

import java.io.Serializable;

import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.registroInt.ParametroRegisInt;
import ec.com.tecnointel.soem.serWebClientSri.autorizacion.RespuestaComprobante;
import ec.com.tecnointel.soem.serWebClientSri.general.AutorizacionDTO;
import ec.com.tecnointel.soem.serWebSri.registroInt.AutorizacionComprobantesWsInt;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class SriClienteWs implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	AutorizacionComprobantesWsInt autorizacionComprobantes;

	@Inject
	ParametroRegisInt parametroRegis;

	Parametro parametroFilasPagina = new Parametro();
	Parametro parametroProxyIp = new Parametro();
	Parametro parametroProxyPuerto = new Parametro();
	Parametro parametroUrlProduccion = new Parametro();
	Parametro parametroUrlPruebas = new Parametro();
	Parametro parametroRutaDescargados = new Parametro();

	public void cargarParametros() throws Exception {
		parametroFilasPagina = parametroRegis.buscarPorId(Parametro.class, 6100);
		parametroProxyIp = parametroRegis.buscarPorId(Parametro.class, 3211);
		parametroProxyPuerto = parametroRegis.buscarPorId(Parametro.class, 3212);
		parametroUrlProduccion = parametroRegis.buscarPorId(Parametro.class, 3220);
		parametroUrlPruebas = parametroRegis.buscarPorId(Parametro.class, 3221);
		parametroRutaDescargados = parametroRegis.buscarPorId(Parametro.class, 4251);
	}

//	@Value("${sri.ws.url}")
//	private String sriWsUrl;
//
//	public String descargarXml(String claveAcceso) {
//		// El SRI responde con un objeto que contiene el XML dentro de "comprobante"
//		try {
//			AutorizacionComprobantesOffline port = obtenerPort();
//			RespuestaComprobante respuesta = port.autorizacionComprobante(claveAcceso);
//
//			return respuesta.getAutorizaciones().getAutorizacion().stream().findFirst()
//					.orElseThrow(() -> new RuntimeException("Sin autorización para clave: " + claveAcceso))
//					.getComprobante(); // aquí viene el XML crudo
//		} catch (Exception e) {
//			throw new RuntimeException("Error al conectar con SRI", e);
//		}
//	}
//
//	private AutorizacionComprobantesOffline obtenerPort() {
//		// generado por wsimport o similar desde el WSDL del SRI
//		AutorizacionComprobantesOfflineService service = new AutorizacionComprobantesOfflineService();
//		return service.getAutorizacionComprobantesOfflinePort();
//	}

//	public String descargarXmlSri(String claveAcceso) throws Exception {
//
//		RespuestaComprobante respuestaComprobante;
//		AutorizacionDTO autorizacionDTO;
//
//		cargarParametros();
//
//		respuestaComprobante = this.verificarAutorizacion(claveAcceso);
//
//		if (!respuestaComprobante.getAutorizaciones().getAutorizacion().isEmpty()) {
//
//			autorizacionDTO = this.obtenerEstadoAutorizacion(respuestaComprobante);
//
//			LocalDateTime fechaAuto = autorizacionDTO.getAutorizacion().getFechaAutorizacion().toGregorianCalendar()
//					.toZonedDateTime().toLocalDateTime();
//
//			System.out.println(fechaAuto);
//
//			// Crea el archivo con la autorizacion y copia a la carpeta autorizados
//			// autorizacionComprobantes.validarRespuestaAutorizacion(autorizacionDTO,
//			// claveAcce, "C:\\saolan\\soem\\comprobantes\\generados\\");
////			autorizacionComprobantes.crearArchivoXml(autorizacionDTO, claveAcceso + ".xml",
////					parametroRutaDescargados.getDescri());
//
////			this.crearArchivoXml(autorizacionDTO, claveAcceso + ".xml",
////					parametroRutaDescargados.getDescri());
//
//		}
//
//		return respuestaComprobante.getAutorizaciones().getAutorizacion().stream().findFirst()
//				.orElseThrow(() -> new RuntimeException("Sin autorización para clave: " + claveAcceso))
//				.getComprobante(); // aquí viene el XML crudo
//
//	}

//	public AutorizacionDTO obtenerAutorizacionDTO(String claveAcceso) throws Exception {
//
//		RespuestaComprobante respuestaComprobante;
//		AutorizacionDTO autorizacionDTO;
//
//		cargarParametros();
//
//		respuestaComprobante = this.verificarAutorizacion(claveAcceso);		
//
//		if (!respuestaComprobante.getAutorizaciones().getAutorizacion().isEmpty()) {
//
////			autorizacionDTO = this.obtenerEstadoAutorizacion(respuestaComprobante);
//
//			autorizacionDTO = autorizacionComprobantes.obtenerEstadoAutorizacion(respuestaComprobante);
//
//			LocalDateTime fechaAuto = autorizacionDTO.getAutorizacion().getFechaAutorizacion().toGregorianCalendar()
//					.toZonedDateTime().toLocalDateTime();
//
//			System.out.println(fechaAuto);
//
//			return autorizacionDTO;
//
//		}
//
//		return autorizacionComprobantes.obtenerEstadoAutorizacion(respuestaComprobante);
//	}

	public RespuestaComprobante verificarAutorizacion(String claveAcceso) throws Exception {

		RespuestaComprobante respuestaComprobante = new RespuestaComprobante();

		respuestaComprobante = autorizacionComprobantes.autorizarComprobante(parametroProxyIp.getDescri(),
				parametroProxyPuerto.getDescri(), "2", parametroUrlProduccion.getDescri(),
				parametroUrlPruebas.getDescri(), "AutorizacionComprobantesOffline", claveAcceso);

		return respuestaComprobante;
	}

	public AutorizacionDTO obtenerEstadoAutorizacion(RespuestaComprobante respuestaComprobante) {

		AutorizacionDTO autorizacionDTO = null;

		try {
			autorizacionDTO = autorizacionComprobantes.obtenerEstadoAutorizacion(respuestaComprobante);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return autorizacionDTO;
	}

//
//	public void crearArchivoXml(AutorizacionDTO autorizacionDTO, String nombreArchivo, String rutaDescargados)
//			throws Exception {
//
//		AutorizacionComprobantesUtil autorizacionComprobantesUtil = new AutorizacionComprobantesUtil(nombreArchivo);
//
//		autorizacionComprobantesUtil.crearArchivoXml(autorizacionDTO, rutaDescargados);
//
//	}
}
