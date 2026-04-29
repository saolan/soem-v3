package ec.com.saolan.soem.compartido.excepcion;

import jakarta.ejb.ApplicationException;

/**
 * Ejemplos:
 * mapearFactura(...) cabecera
 * mapearFactura(...) detalle
 */
@ApplicationException(rollback = false, inherited = true)
public class IntegracionExcepcion extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public IntegracionExcepcion(String message) {
        super(message);
    }

    public IntegracionExcepcion(String message, Throwable cause) {
        super(message, cause);
    }
}