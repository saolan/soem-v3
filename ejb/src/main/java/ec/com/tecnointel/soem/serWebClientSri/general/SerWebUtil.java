package ec.com.tecnointel.soem.serWebClientSri.general;

import java.net.MalformedURLException;

import jakarta.xml.ws.WebServiceException;

/**
 * @author SandRoland
 * 
 * Esta clase Equivale a FormGenerales
 *
 */

public class SerWebUtil {
	
	public static String devuelveUrlWs(String proxyIpUrl, String puerto, String ambiente, String urlProduccion,
			String urlPruebas, String nombreServicio) {

		StringBuilder url = new StringBuilder();
		String direccionIPServicio = null;

		// Proxy configuracion = null;
		// try {
		// configuracion = new ProxySQL().obtenerProxy();
		// } catch (Exception ex) {
		// Logger.getLogger(FormGenerales.class.getName()).log(Level.SEVERE, null, ex);
		// }

		// if (configuracion != null) {

//		if (proxyIpUrl != null || proxyIpUrl != "") {
//			String uri = url + ":" + puerto;
//			ProxyConfig.init(uri);
//		}

		// Ambiente Produccion = 2; Pruebas = 1
		if (ambiente.equals("2")) {
			direccionIPServicio = urlProduccion;
		} else if (ambiente.equals("1")) {
			direccionIPServicio = urlPruebas;
		}

		url.append(direccionIPServicio);

		url.append("/comprobantes-electronicos-ws/");
		url.append(nombreServicio);
		url.append("?wsdl");

		// }

		return url.toString();

	}
	
	public static Boolean validarUrl(String proxyIpUrl, String puerto, String ambiente, String urlProduccion,
			String urlPruebas, String nombreServicio, String rutaCertificadoSerWebSri) throws Exception {

		Boolean urlValido = true;
		String mensaje = null;

		String url = devuelveUrlWs(proxyIpUrl, puerto, ambiente, urlProduccion, urlPruebas, nombreServicio);
		
//		Esto esta en ProxyConfig		
//		System.setProperty("javax.net.ssl.keyStore", "C:\\Program Files\\Java\\jdk1.8.0_181\\jre\\lib\\security\\cacerts");
//		System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
//		System.setProperty("javax.net.ssl.trustStore", "C:\\Program Files\\Java\\jdk1.8.0_181\\jre\\lib\\security\\cacerts");
//		System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

		System.setProperty("javax.net.ssl.keyStore", rutaCertificadoSerWebSri);
		System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
		System.setProperty("javax.net.ssl.trustStore", rutaCertificadoSerWebSri);
		System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

		Object c = EnvioComprobantesWs.webService(url);

		if ((c instanceof MalformedURLException)) {

			mensaje = ((MalformedURLException) c).getMessage();
			urlValido = false;

		}

		// return mensaje;
		return urlValido;
	}

	public static Boolean existeConexion(String proxyIpUrl, String puerto, String ambiente, String urlProduccion,
			String urlPruebas, String nombreServicio, String rutaCertificadoSerWebSri) throws Exception {

		String url = devuelveUrlWs(proxyIpUrl, puerto, ambiente, urlProduccion, urlPruebas, nombreServicio);
		
	System.setProperty("javax.net.ssl.keyStore", rutaCertificadoSerWebSri);
	System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
	System.setProperty("javax.net.ssl.trustStore", rutaCertificadoSerWebSri);
	System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
	
		int i = 0;

		// Boolean respuesta = Boolean.valueOf(false);
		Boolean respuesta = false;

		while (i < 3) {

			Object c = EnvioComprobantesWs.webService(url);

			if (c == null) {
				respuesta = true;
				break;
			}

			if ((c instanceof WebServiceException)) {
				respuesta = false;
			}

			i++;
		}

		return respuesta;
	}
}
