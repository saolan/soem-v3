package ec.com.tecnointel.soem.serWebClientSri.general;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;

import ec.com.tecnointel.soem.general.util.Util;
import ec.com.tecnointel.soem.serWebClientSri.recepcion.Comprobante;
import ec.com.tecnointel.soem.serWebClientSri.recepcion.Mensaje;
import ec.com.tecnointel.soem.serWebClientSri.recepcion.RecepcionComprobantesOffline;
import ec.com.tecnointel.soem.serWebClientSri.recepcion.RecepcionComprobantesOfflineService;
import ec.com.tecnointel.soem.serWebClientSri.recepcion.RespuestaSolicitud;
import jakarta.xml.ws.WebServiceException;

public class EnvioComprobantesWs {

	private static RecepcionComprobantesOfflineService service;
//	private static final String VERSION = "1.0.0";
//	public static final String ESTADO_RECIBIDA = "RECIBIDA";
//	public static final String ESTADO_DEVUELTA = "DEVUELTA";

	public EnvioComprobantesWs(String wsdlLocation) throws MalformedURLException, WebServiceException {
		URL url = new URL(wsdlLocation);
		QName qname = new QName("http://ec.gob.sri.ws.recepcion", "RecepcionComprobantesOfflineService");
		service = new RecepcionComprobantesOfflineService(url, qname);
	}

	public static final Object webService(String wsdlLocation) {
		try {
			QName qname = new QName("http://ec.gob.sri.ws.recepcion", "RecepcionComprobantesOfflineService");
			URL url = new URL(wsdlLocation);
			service = new RecepcionComprobantesOfflineService(url, qname);
			return null;
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
			return ex;
		} catch (WebServiceException ws) {
			ws.printStackTrace();
			return ws;
		}
	}

	public RespuestaSolicitud enviarComprobante(String ruc, File xmlFile, String tipoComprobante, String versionXsd) {
		RespuestaSolicitud respuestaSolicitud = null;
		try {
			RecepcionComprobantesOffline port = service.getRecepcionComprobantesOfflinePort();
			respuestaSolicitud = port.validarComprobante(Util.archivoToByte(xmlFile));
		} catch (Exception e) {
			e.printStackTrace();
			respuestaSolicitud = new RespuestaSolicitud();
			respuestaSolicitud.setEstado(e.getMessage());
			return respuestaSolicitud;
		}
		return respuestaSolicitud;
	}

	public RespuestaSolicitud enviarComprobanteLotes(String ruc, byte[] xml, String tipoComprobante,
			String versionXsd) {
		RespuestaSolicitud response = null;
		try {
			RecepcionComprobantesOffline port = service.getRecepcionComprobantesOfflinePort();

			response = port.validarComprobante(xml);
		} catch (Exception e) {
			e.printStackTrace();
			response = new RespuestaSolicitud();
			response.setEstado(e.getMessage());
			return response;
		}
		return response;
	}

	public RespuestaSolicitud enviarComprobanteLotes(String ruc, File xml, String tipoComprobante, String versionXsd) {
		RespuestaSolicitud response = null;
		try {
			RecepcionComprobantesOffline port = service.getRecepcionComprobantesOfflinePort();
			response = port.validarComprobante(Util.archivoToByte(xml));
		} catch (Exception e) {
			e.printStackTrace();
			response = new RespuestaSolicitud();
			response.setEstado(e.getMessage());
			return response;
		}
		return response;
	}

	public static RespuestaSolicitud obtenerRespuestaEnvio(File archivo, String ruc, String tipoComprobante,
			String claveDeAcceso, String urlWsdl) {
		RespuestaSolicitud respuesta = new RespuestaSolicitud();
		EnvioComprobantesWs cliente = null;
		try {
			cliente = new EnvioComprobantesWs(urlWsdl);
		} catch (Exception e) {
			e.printStackTrace();
			respuesta.setEstado(e.getMessage());
			return respuesta;
		}
		respuesta = cliente.enviarComprobante(ruc, archivo, tipoComprobante, "1.0.0");

		return respuesta;
	}

	public static RespuestaSolicitud obtenerRespuestaEnvio1(File archivo, String ruc, String tipoComprobante,
			String claveDeAcceso, String urlWsdl) {
		
		RespuestaSolicitud respuestaSolicitud = new RespuestaSolicitud();
		EnvioComprobantesWs cliente = null;
		try {
			cliente = new EnvioComprobantesWs(urlWsdl);
		} catch (Exception e) {
			e.printStackTrace();
			respuestaSolicitud.setEstado(e.getMessage());
			return respuestaSolicitud;
		}
		
		respuestaSolicitud = cliente.enviarComprobante(ruc, archivo, tipoComprobante, "1.0.0");

		return respuestaSolicitud;
	}

//	public static void guardarRespuesta(String claveDeAcceso, String archivo, String estado, java.util.Date fecha) {
//		try {
//			java.sql.Date sqlDate = new java.sql.Date(fecha.getTime());
//
//			// TODO: Grabar en base de datos
//			// Respuesta item = new Respuesta(null, claveDeAcceso, archivo, estado,
//			// sqlDate);
//			// RespuestaSQL resp = new RespuestaSQL();
//			// resp.insertarRespuesta(item);
//		} catch (Exception ex) {
//			// Logger.getLogger(EnvioComprobantesWs.class.getName()).log(Level.SEVERE, null,
//			// ex);
//		}
//	}

//	Se reemplaza \n por ;
	public static String obtenerMensajeRespuesta(RespuestaSolicitud respuesta) {
		
		StringBuilder mensajeDesplegable = new StringBuilder();
		
		if (respuesta.getEstado().equals("DEVUELTA") == true) {
			
			RespuestaSolicitud.Comprobantes comprobantes = respuesta.getComprobantes();
			
			for (Comprobante comp : comprobantes.getComprobante()) {
				
				mensajeDesplegable.append(comp.getClaveAcceso());
				mensajeDesplegable.append(";"); 
				
				for (Mensaje m : comp.getMensajes().getMensaje()) {
					
					mensajeDesplegable.append(m.getMensaje()).append(";");
					mensajeDesplegable.append(m.getInformacionAdicional() != null ? m.getInformacionAdicional() : "");
					mensajeDesplegable.append(";");
				}
				mensajeDesplegable.append(";");
			}
		}
		return mensajeDesplegable.toString();
	}
	
}
