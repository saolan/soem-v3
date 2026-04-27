package ec.com.saolan.soem.sri.aplicacion.importacion.factura;

import java.io.Serializable;

import ec.com.saolan.soem.sri.infraestructura.importacion.ImportarDocumeElecSriParametros;
import ec.com.saolan.soem.sri.infraestructura.importacion.ImportarDocumeElecSriServicio;
import ec.com.saolan.soem.sri.infraestructura.unmarshaller.DocumentoSriUnmarshaller;
import ec.com.saolan.soem.sri.infraestructura.unmarshaller.FacturaSriUnmarshaller;
import ec.com.tecnointel.soem.documeElec.modelo.factura.Factura;
import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import ec.com.tecnointel.soem.serWebClientSri.general.AutorizacionDTO;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class ImportarFacturaSriServicio extends ImportarDocumeElecSriServicio<Ingreso, Factura>
		implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	FacturaSriUnmarshaller facturaSriUnmarshaller;

	@Inject
	IngresoXmlMapeador IngresoXmlMapeador;

	@Override
	protected DocumentoSriUnmarshaller<Factura> getUnmarshaller() {
		return facturaSriUnmarshaller;
	}

	@Override
	protected String codigoDocumentoEsperado() {
		return CODIGO_FACTURA;
	}

	@Override
	protected Ingreso mapearDocumento(Factura factura, AutorizacionDTO autorizacionDTO,
			ImportarDocumeElecSriParametros importarDocumeElecSriParametros) {
		return IngresoXmlMapeador.mapearFactura(factura, autorizacionDTO, importarDocumeElecSriParametros);
	}

//	LLama al metodo descargar de la clase abstracta
	public Ingreso importarFacturaSri(String claveAcceso,
			ImportarDocumeElecSriParametros importarDocumeElecSriParametros) {
		return importar(claveAcceso, importarDocumeElecSriParametros);
	}
}
