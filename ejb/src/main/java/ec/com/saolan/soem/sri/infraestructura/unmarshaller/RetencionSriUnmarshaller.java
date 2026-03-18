package ec.com.saolan.soem.sri.infraestructura.unmarshaller;

import java.io.StringReader;

import ec.com.tecnointel.soem.documeElec.modelo.retencion.ComprobanteRetencion;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

public class RetencionSriUnmarshaller implements SriUnmarshaller<ComprobanteRetencion> {

	@Override
	public ComprobanteRetencion unmarshall(String xml) {
		try {
			JAXBContext context = JAXBContext.newInstance(ComprobanteRetencion.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (ComprobanteRetencion) unmarshaller.unmarshal(new StringReader(xml));
		} catch (JAXBException e) {
			throw new RuntimeException("Error al procesar XML de Retencion SRI", e);
		}
	}
}
