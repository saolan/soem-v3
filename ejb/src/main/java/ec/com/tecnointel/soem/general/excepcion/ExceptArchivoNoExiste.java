package ec.com.tecnointel.soem.general.excepcion;

public class ExceptArchivoNoExiste extends Exception {

	private static final long serialVersionUID = -2229017483590199148L;

	public ExceptArchivoNoExiste(String mensaje) {
		super(mensaje);
	}
}
