package ec.com.saolan.soem.compartido.excepcion;

import jakarta.ejb.ApplicationException;

@ApplicationException(rollback = true, inherited = true)
public class SeguridadExcepcion extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public SeguridadExcepcion(String message) {
		super(message);
	}

	public SeguridadExcepcion(String message, Throwable cause) {
		super(message, cause);
	}
}
