package ec.com.tecnointel.soem.serWebClientSri.autorizacion;

import java.net.URL;

import javax.xml.namespace.QName;

import jakarta.xml.ws.Service;
import jakarta.xml.ws.WebEndpoint;
import jakarta.xml.ws.WebServiceClient;
import jakarta.xml.ws.WebServiceException;
import jakarta.xml.ws.WebServiceFeature;

@WebServiceClient(name = "AutorizacionComprobantesOfflineService", targetNamespace = "http://ec.gob.sri.ws.autorizacion", wsdlLocation = "/META-INF/wsdl/AutorizacionComprobantes.wsdl")
public class AutorizacionComprobantesOfflineService extends Service {
	
	private static final URL AUTORIZACIONCOMPROBANTESSERVICE_WSDL_LOCATION;
	private static final WebServiceException AUTORIZACIONCOMPROBANTESSERVICE_EXCEPTION;
	private static final QName AUTORIZACIONCOMPROBANTESSERVICE_QNAME = new QName("http://ec.gob.sri.ws.autorizacion",
			"AutorizacionComprobantesOfflineService");

	static {
		AUTORIZACIONCOMPROBANTESSERVICE_WSDL_LOCATION = AutorizacionComprobantesOfflineService.class
				.getResource("/META-INF/wsdl/AutorizacionComprobantes.wsdl");
		WebServiceException e = null;
		if (AUTORIZACIONCOMPROBANTESSERVICE_WSDL_LOCATION == null) {
			e = new WebServiceException(
					"Cannot find '/META-INF/wsdl/AutorizacionComprobantes.wsdl' wsdl. Place the resource correctly in the classpath.");
		}
		AUTORIZACIONCOMPROBANTESSERVICE_EXCEPTION = e;
	}

	public AutorizacionComprobantesOfflineService() {
		super(__getWsdlLocation(), AUTORIZACIONCOMPROBANTESSERVICE_QNAME);
	}

	public AutorizacionComprobantesOfflineService(URL wsdlLocation) {
		super(wsdlLocation, AUTORIZACIONCOMPROBANTESSERVICE_QNAME);
	}

	public AutorizacionComprobantesOfflineService(URL wsdlLocation, QName serviceName) {
		super(wsdlLocation, serviceName);
	}

	@WebEndpoint(name = "AutorizacionComprobantesOfflinePort")
	public AutorizacionComprobantesOffline getAutorizacionComprobantesOfflinePort() {
		return (AutorizacionComprobantesOffline) super.getPort(
				new QName("http://ec.gob.sri.ws.autorizacion", "AutorizacionComprobantesOfflinePort"),
				AutorizacionComprobantesOffline.class);
	}

	@WebEndpoint(name = "AutorizacionComprobantesOfflinePort")
	public AutorizacionComprobantesOffline getAutorizacionComprobantesOfflinePort(WebServiceFeature... features) {
		return (AutorizacionComprobantesOffline) super.getPort(
				new QName("http://ec.gob.sri.ws.autorizacion", "AutorizacionComprobantesOfflinePort"),
				AutorizacionComprobantesOffline.class, features);
	}

	private static URL __getWsdlLocation() {
		if (AUTORIZACIONCOMPROBANTESSERVICE_EXCEPTION != null) {
			throw AUTORIZACIONCOMPROBANTESSERVICE_EXCEPTION;
		}
		return AUTORIZACIONCOMPROBANTESSERVICE_WSDL_LOCATION;
	}
}
