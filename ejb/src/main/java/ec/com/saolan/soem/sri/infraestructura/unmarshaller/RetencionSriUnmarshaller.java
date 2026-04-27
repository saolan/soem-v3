package ec.com.saolan.soem.sri.infraestructura.unmarshaller;

import java.io.StringReader;

import ec.com.saolan.soem.sri.infraestructura.excepcion.XmlUnmarshallExcepcion;
import ec.com.tecnointel.soem.documeElec.modelo.retencion.ComprobanteRetencion;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

@ApplicationScoped
public class RetencionSriUnmarshaller implements DocumentoSriUnmarshaller<ComprobanteRetencion> {

	private static final JAXBContext JAXB_CONTEXT = createContext();

    @Override
    public ComprobanteRetencion unmarshallDocumento(String xml) {

    	if (xml == null || xml.isBlank()) {
            throw new IllegalArgumentException("El XML de Retención SRI no puede ser nulo o vacío");
        }

        try (StringReader reader = new StringReader(xml)) {
            Unmarshaller unmarshaller = JAXB_CONTEXT.createUnmarshaller();
            return (ComprobanteRetencion) unmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            throw new XmlUnmarshallExcepcion("Error al procesar XML de Retención SRI", e);
        }
    }
	
    private static JAXBContext createContext() {
        try {
            return JAXBContext.newInstance(ComprobanteRetencion.class);
        } catch (JAXBException e) {
            throw new IllegalStateException(
                "No fue posible inicializar JAXBContext para ComprobanteRetencion", e
            );
        }
    }
}