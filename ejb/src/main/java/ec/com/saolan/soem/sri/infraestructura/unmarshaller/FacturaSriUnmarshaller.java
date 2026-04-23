package ec.com.saolan.soem.sri.infraestructura.unmarshaller;

import java.io.StringReader;

import ec.com.saolan.soem.sri.infraestructura.aplicacion.compartido.DocumentoSriUnmarshaller;
import ec.com.saolan.soem.sri.infraestructura.excepcion.XmlUnmarshallExcepcion;
import ec.com.tecnointel.soem.documeElec.modelo.factura.Factura;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

@ApplicationScoped
public class FacturaSriUnmarshaller implements DocumentoSriUnmarshaller<Factura> {

	private static final JAXBContext JAXB_CONTEXT = createContext();

    @Override
    public Factura unmarshallDocumento(String xml) {

    	if (xml == null || xml.isBlank()) {
            throw new IllegalArgumentException("El XML de Factura SRI no puede ser nulo o vacío");
        }

        try (StringReader reader = new StringReader(xml)) {
            Unmarshaller unmarshaller = JAXB_CONTEXT.createUnmarshaller();
            return (Factura) unmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            throw new XmlUnmarshallExcepcion("Error al procesar XML de Retención SRI", e);
        }
    }
	
    private static JAXBContext createContext() {
        try {
            return JAXBContext.newInstance(Factura.class);
        } catch (JAXBException e) {
            throw new IllegalStateException(
                "No fue posible inicializar JAXBContext para factura", e
            );
        }
    }
}
