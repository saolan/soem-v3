package ec.com.tecnointel.soem.serWebClientSri.general;

import java.net.URL;

import javax.xml.namespace.QName;

import ec.com.tecnointel.soem.serWebClientSri.autorizacion.AutorizacionComprobantesOffline;
import ec.com.tecnointel.soem.serWebClientSri.autorizacion.AutorizacionComprobantesOfflineService;
import ec.com.tecnointel.soem.serWebClientSri.autorizacion.RespuestaComprobante;

public class AutorizacionComprobantesWs {

	private AutorizacionComprobantesOfflineService service;
	public static final String ESTADO_AUTORIZADO = "AUTORIZADO";
	public static final String ESTADO_EN_PROCESO = "EN PROCESO";
	public static final String ESTADO_NO_AUTORIZADO = "NO AUTORIZADO";

	public AutorizacionComprobantesWs(String wsdlLocation) {
		
		try {
			
			this.service = new AutorizacionComprobantesOfflineService(new URL(wsdlLocation),
					new QName("http://ec.gob.sri.ws.autorizacion", "AutorizacionComprobantesOfflineService"));
			
		} catch (Exception e) {
			e.printStackTrace();
			// Logger.getLogger(AutorizacionComprobantesWs.class.getName()).log(Level.SEVERE,
			// null, ex);
			// JOptionPane.showMessageDialog(new JFrame(), ex.getMessage(), "Se ha producido
			// un error ", 0);
		}
	}

	public RespuestaComprobante llamadaWSAutorizacionInd(String claveDeAcceso) {
		
		RespuestaComprobante respuestaComprobante = null;
		
		try {
			
			AutorizacionComprobantesOffline port = this.service.getAutorizacionComprobantesOfflinePort();
			respuestaComprobante = port.autorizacionComprobante(claveDeAcceso);
			
		} catch (Exception e) {
			e.printStackTrace();
			return respuestaComprobante;
		}
		return respuestaComprobante;
	}
	      
//	public RespuestaLote llamadaWsAutorizacionLote(String claveDeAcceso) {
//		
//		RespuestaLote response = null;
//		
//		try {
//			
//			AutorizacionComprobantesOffline port = this.service.getAutorizacionComprobantesOfflinePort();
//			response = port.autorizacionComprobanteLote(claveDeAcceso);
//			
//		} catch (Exception e) {
//			
//			// Logger.getLogger(AutorizacionComprobantesWs.class.getName()).log(Level.SEVERE,
//			// null, e);
//			e.printStackTrace();
//			return response;
//		}
//		return response;
//	}

	// Original SRI
	// public static RespuestaComprobante autorizarComprobante(String claveDeAcceso,
	// String tipoAmbiente)
	// throws RespuestaAutorizacionException {
	// return new AutorizacionComprobantesWs(
	// SerWebUtil.devuelveUrlWs(tipoAmbiente, "AutorizacionComprobantesOffline"))
	// .llamadaWSAutorizacionInd(claveDeAcceso);
	// }

	public String urlSerWeb (String proxyIpUrl, String puerto, String ambiente, String urlProduccion,
			String urlPruebas, String nombreServicio) {
		
		return SerWebUtil.devuelveUrlWs(proxyIpUrl, puerto, ambiente, urlProduccion, urlPruebas, nombreServicio);
		
	}
	
	public static RespuestaComprobante autorizarComprobante(String proxyIpUrl, String puerto, String ambiente,
			String urlProduccion, String urlPruebas, String nombreServicio, String claveAcce) throws RespuestaAutorizacionException {
	
//		String url = SerWebUtil.devuelveUrlWs(proxyIpUrl, puerto, ambiente, urlProduccion, urlPruebas, nombreServicio);		
//		
//		AutorizacionComprobantesWs autorizacionComprobantesWs = new AutorizacionComprobantesWs(url);
//		
//		RespuestaComprobante respuestaComprobante = autorizacionComprobantesWs.llamadaWSAutorizacionInd("1209201801171312262800110010010000003720325071819");
//		
//		return respuestaComprobante;
		
		return new AutorizacionComprobantesWs(
				SerWebUtil.devuelveUrlWs(proxyIpUrl, puerto, ambiente, urlProduccion, urlPruebas, nombreServicio))
						.llamadaWSAutorizacionInd(claveAcce); 
	}

	// public static String autorizarComprobanteIndividual(String claveDeAcceso,
	// String nombreArchivo, String tipoAmbiente)
	// {
	// StringBuilder mensaje = new StringBuilder();
	// try
	// {
	// String dirAutorizados = "TempAutorizado"; //new
	// ConfiguracionDirectorioSQL().obtenerDirectorio(DirectorioEnum.AUTORIZADOS.getCode()).getPath();
	// String dirNoAutorizados = "TempNoAutorizado"; //new
	// ConfiguracionDirectorioSQL().obtenerDirectorio(DirectorioEnum.NO_AUTORIZADOS.getCode()).getPath();
	//
	// RespuestaComprobante respuesta = null;
	// for (int i = 0; i < 5; i++)
	// {
	// respuesta = new
	// AutorizacionComprobantesWs(FormGenerales.devuelveUrlWs(tipoAmbiente,
	// "AutorizacionComprobantesOffline")).llamadaWSAutorizacionInd(claveDeAcceso);
	// if (!respuesta.getAutorizaciones().getAutorizacion().isEmpty()) {
	// break;
	// }
	// Thread.currentThread();Thread.sleep(300L);
	// }
	// if (respuesta != null)
	// {
	// int i = 0;
	// for (Autorizacion item : respuesta.getAutorizaciones().getAutorizacion())
	// {
	// mensaje.append(item.getEstado());
	//
	// item.setComprobante("<![CDATA[" + item.getComprobante() + "]]>");
	//
	// XStream xstream = XStreamUtil.getRespuestaXStream();
	// Writer writer = null;
	// ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	// writer = new OutputStreamWriter(outputStream, "UTF-8");
	// writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	//
	// xstream.toXML(item, writer);
	// String xmlAutorizacion = outputStream.toString("UTF-8");
	// if ((i == 0) && (item.getEstado().equals("AUTORIZADO")))
	// {
	// Util.stringToArchivo(dirAutorizados + File.separator + nombreArchivo,
	// xmlAutorizacion);
	// VisualizacionRideUtil.decodeArchivoBase64(dirAutorizados + File.separator +
	// nombreArchivo, item.getNumeroAutorizacion(),
	// item.getFechaAutorizacion().toString());
	// break;
	// }
	// if (item.getEstado().equals("NO AUTORIZADO"))
	// {
	// Util.stringToArchivo(dirNoAutorizados + File.separator + nombreArchivo,
	// xmlAutorizacion);
	// mensaje.append("|" + obtieneMensajesAutorizacion(item));
	//
	//// verificarOCSP(item);
	//
	// break;
	// }
	// if (item.getEstado().equals("EN PROCESO"))
	// {
	// mensaje.append("|" + obtieneMensajesAutorizacion(item));
	// break;
	// }
	// i++;
	// }
	// }
	// }
	// catch (Exception e)
	// {
	//
	// e.printStackTrace();
	//// String dirAutorizados;
	//// String dirNoAutorizados;
	//// int i;
	//
	//// Logger.getLogger(AutorizacionComprobantesWs.class.getName()).log(Level.SEVERE,
	// null, ex);
	// }
	// return mensaje.toString();
	// }

	// public static List<Autorizacion> autorizarComprobanteLote(String
	// claveDeAcceso, String nombreArchivoLote, int timeout, int cantidadArchivos,
	// String tipoAmbiente)
	// {
	// List<Autorizacion> autorizaciones = new ArrayList<>();
	// RespuestaLote respuestaAutorizacion = null;
	// try
	// {
	// String dirAutorizados = "TempAutorizado"; //= new
	// ConfiguracionDirectorioSQL().obtenerDirectorio(DirectorioEnum.AUTORIZADOS.getCode()).getPath();
	// String dirRechazados = "TempNoAutorizado"; //= new
	// ConfiguracionDirectorioSQL().obtenerDirectorio(DirectorioEnum.NO_AUTORIZADOS.getCode()).getPath();
	// String dirFirmados = "TempFirmado";//= new
	// ConfiguracionDirectorioSQL().obtenerDirectorio(DirectorioEnum.FIRMADOS.getCode()).getPath();
	//
	// Thread.currentThread();Thread.sleep(timeout * cantidadArchivos * 1000);
	// for (int i = 0; i < 5; i++)
	// {
	// respuestaAutorizacion = new
	// AutorizacionComprobantesWs(FormGenerales.devuelveUrlWs(tipoAmbiente,
	// "AutorizacionComprobantesOffline")).llamadaWsAutorizacionLote(claveDeAcceso);
	// if (!respuestaAutorizacion.getAutorizaciones().getAutorizacion().isEmpty()) {
	// break;
	// }
	// Thread.currentThread();Thread.sleep(500L);
	// }
	// String comprobantesProcesados =
	// respuestaAutorizacion.getNumeroComprobantesLote();
	// for (Autorizacion item :
	// respuestaAutorizacion.getAutorizaciones().getAutorizacion())
	// {
	// item.setComprobante("<![CDATA[" + item.getComprobante() + "]]>");
	// String claveAcceso = ArchivoUtils.obtieneClaveAccesoAutorizacion(item);
	//
	// XStream xstream = XStreamUtil.getRespuestaLoteXStream();
	// Writer writer = null;
	// ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	// writer = new OutputStreamWriter(outputStream, "UTF-8");
	// writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	//
	// xstream.toXML(item, writer);
	//
	// String nombreArchivoConRespuesta = claveAcceso + ".xml";
	// String xmlAutorizacion = outputStream.toString("UTF-8");
	// if (item.getEstado().equals("AUTORIZADO")) {
	// Util.stringToArchivo(dirAutorizados + File.separator +
	// nombreArchivoConRespuesta, xmlAutorizacion);
	// } else {
	// Util.stringToArchivo(dirRechazados + File.separator +
	// nombreArchivoConRespuesta, xmlAutorizacion);
	// }
	// File archivoABorrar = new File(dirFirmados + File.separator +
	// nombreArchivoConRespuesta);
	// if (archivoABorrar.exists() == true) {
	// archivoABorrar.delete();
	// }
	// item.setEstado(nombreArchivoConRespuesta + "|" + item.getEstado() + "|" +
	// comprobantesProcesados);
	// autorizaciones.add(item);
	// }
	// }
	// catch (Exception e)
	// {
	// e.printStackTrace();
	//// String dirAutorizados;
	//// String dirRechazados;
	//// String dirFirmados;
	//// String comprobantesProcesados;
	//// Logger.getLogger(AutorizacionComprobantesWs.class.getName()).log(Level.SEVERE,
	// null, ex);
	// }
	// return autorizaciones;
	// }

	// public static String obtieneMensajesAutorizacion(Autorizacion autorizacion)
	// {
	// StringBuilder mensaje = new StringBuilder();
	// for (Mensaje m : autorizacion.getMensajes().getMensaje()) {
	// if (m.getInformacionAdicional() != null) {
	// mensaje.append("\n" + m.getMensaje() + ": " + m.getInformacionAdicional());
	// } else {
	// mensaje.append("\n" + m.getMensaje());
	// }
	// }
	// return mensaje.toString();
	// }

	// public static boolean verificarOCSP(Autorizacion autorizacion)
	// throws SQLException, ClassNotFoundException
	// {
	// boolean respuesta = true;
	// for (Mensaje m : autorizacion.getMensajes().getMensaje()) {
	// if (m.getIdentificador().equals("61"))
	// {
	// int i = JOptionPane.showConfirmDialog(null, "No se puede validar el
	// certificado digital.\n Desea emitir en contingencia?", "Advertencia", 0);
	// if (i == 0)
	// {
	// Emisor emisor = new EmisorSQL().obtenerDatosEmisor();
	// FormGenerales.actualizaEmisor(TipoEmisionEnum.PREAUTORIZADA.getCode(),
	// emisor);
	// }
	// respuesta = false;
	// }
	// }
	// return respuesta;
	// }
}
