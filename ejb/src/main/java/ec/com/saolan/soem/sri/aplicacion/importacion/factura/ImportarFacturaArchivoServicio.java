package ec.com.saolan.soem.sri.aplicacion.importacion.factura;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ec.com.saolan.soem.compartido.cache.ParametroRutaCache;
import ec.com.saolan.soem.compartido.excepcion.InfraestructuraExcepcion;
import ec.com.saolan.soem.sri.infraestructura.importacion.ImportarDocumeElecSriParametros;
import ec.com.saolan.soem.sri.infraestructura.unmarshaller.FacturaSriUnmarshaller;
import ec.com.tecnointel.soem.documeElec.modelo.factura.Factura;
import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import ec.com.tecnointel.soem.serWebClientSri.general.AutorizacionDTO;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class ImportarFacturaArchivoServicio implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	FacturaSriUnmarshaller facturaSriUnmarshaller;

	@Inject
	IngresoXmlMapeador mapeadorFacturaSriServicio;

	@Inject
	private ParametroRutaCache parametroRutaCache;

	public Ingreso importarXmlDesdeArchivo(String claveAcce,
			ImportarDocumeElecSriParametros importarDocumeElecSriParametros) throws InfraestructuraExcepcion, Exception {

		File file = new File(parametroRutaCache.getRutaDescargados().getDescri() + claveAcce + ".xml");

		if (!file.exists()) {
			throw new InfraestructuraExcepcion("Documento " + claveAcce + " no Existe");
		}

		// Separa el comprobante de la autorizacion
		String comprobante = separarComprobante(
				parametroRutaCache.getRutaDescargados().getDescri() + claveAcce + ".xml");

		Factura factura = facturaSriUnmarshaller.unmarshallDocumento(comprobante);

		Ingreso ingreso = mapearDocumento(factura, null, importarDocumeElecSriParametros);

		return ingreso;
	}

	protected Ingreso mapearDocumento(Factura factura, AutorizacionDTO autorizacionDTO,
			ImportarDocumeElecSriParametros importarDocumeElecSriParametros) {
		return mapeadorFacturaSriServicio.mapearFactura(factura, autorizacionDTO, importarDocumeElecSriParametros);
	}

	/**
	 * Devuelve en un string solo el contenido del comprobante separando de la
	 * autorizacion
	 */
	public String separarComprobante(String rutaDescargados) {

		String cdataNode = "comprobante";

		String comprobante = "";

		try (InputStream in = new BufferedInputStream(new FileInputStream(rutaDescargados))) {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(in);
			NodeList elements = doc.getElementsByTagName(cdataNode);

			for (int i = 0; i < elements.getLength(); i++) {
				Node e = elements.item(i);
				comprobante = e.getTextContent();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return comprobante;
	}
}
