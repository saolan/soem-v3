package ec.com.saolan.soem.sri.aplicacion.importacion.retencion;

import java.io.Serializable;

import ec.com.saolan.soem.compartido.cache.ParametroConexionSriCache;
import ec.com.saolan.soem.sri.infraestructura.importacion.ImportarDocumeElecSriParametros;
import ec.com.saolan.soem.sri.infraestructura.importacion.ImportarDocumeElecSriServicio;
import ec.com.saolan.soem.sri.infraestructura.unmarshaller.DocumentoSriUnmarshaller;
import ec.com.saolan.soem.sri.infraestructura.unmarshaller.RetencionSriUnmarshaller;
import ec.com.tecnointel.soem.documeElec.modelo.retencion.ComprobanteRetencion;
import ec.com.tecnointel.soem.ingreso.modelo.Retencion;
import ec.com.tecnointel.soem.serWebClientSri.general.AutorizacionDTO;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class ImportarRetencionSriServicio extends ImportarDocumeElecSriServicio<Retencion, ComprobanteRetencion>
		implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	RetencionSriUnmarshaller retencionSriUnmarshaller;

	@Inject
	RetencionXmlMapeador retencionXmlMapeador;

	@Inject
	ParametroConexionSriCache parametroDocuElectronicoCache;

	@Override
	protected DocumentoSriUnmarshaller<ComprobanteRetencion> getUnmarshaller() {
		return retencionSriUnmarshaller;
	}

	/*
	 * En este caso recive esta clase (importarDocumeElecSriParametros) vacia, no es
	 * necesario ningun parametro
	 * 
	 */
	@Override
	protected Retencion mapearDocumento(ComprobanteRetencion comprobanteRetencion, AutorizacionDTO autorizacionDTO,
			ImportarDocumeElecSriParametros importarDocumeElecSriParametros) {
		return mapearRetencion(comprobanteRetencion, autorizacionDTO, importarDocumeElecSriParametros);
	}

	@Override
	protected String codigoDocumentoEsperado() {
		return CODIGO_RETENCION;
	}

//	LLama al metodo descargar de la clase abstracta
	public Retencion importarRetencionSri(String claveAcceso,
			ImportarDocumeElecSriParametros importarDocumeElecSriParametros) {
		return importar(claveAcceso, importarDocumeElecSriParametros);
	}

	public Retencion mapearRetencion(ComprobanteRetencion comprobanteRetencion, AutorizacionDTO autorizacionDTO,
			ImportarDocumeElecSriParametros importarDocumeElecSriParametros) {
		return retencionXmlMapeador.mapearRetencion(comprobanteRetencion, autorizacionDTO);
	}
}
