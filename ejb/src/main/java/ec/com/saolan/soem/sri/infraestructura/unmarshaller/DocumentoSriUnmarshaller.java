package ec.com.saolan.soem.sri.infraestructura.unmarshaller;

public interface DocumentoSriUnmarshaller<D> {
	D unmarshallDocumento(String xml);
}
