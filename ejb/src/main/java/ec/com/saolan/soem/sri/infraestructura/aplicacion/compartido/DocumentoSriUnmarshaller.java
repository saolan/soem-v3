package ec.com.saolan.soem.sri.infraestructura.aplicacion.compartido;

public interface DocumentoSriUnmarshaller<D> {
	D unmarshallDocumento(String xml);
}
