package ec.com.tecnointel.soem.parametro.controlador;

public class PersonaException extends Exception{

	private static final long serialVersionUID = 124618907662183004L;

	public PersonaException(String mensaje) {
		super(mensaje);
	}
	
	public PersonaException(String mensaje, Throwable error) {
		super(mensaje, error);
	}

	public PersonaException(String mensaje, Exception e) {
		super(mensaje, e);
	}
}

