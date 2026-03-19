package ec.com.saolan.soem.sri.infraestructura.excepcion;

public class XmlUnmarshallExcepcion extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public XmlUnmarshallExcepcion(String message, Throwable cause) {
        super(message, cause);
    }
}
