package ec.com.saolan.soem.compartido.excepcion;

import jakarta.ejb.ApplicationException;

/**
 * Ejemplos:
 * buscarRolPrecPredet(...)
 * buscarProdPrecs(...)
 * insertarPersProv(...)
 */
@ApplicationException(rollback = true, inherited = true)	
public class InfraestructuraExcepcion extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InfraestructuraExcepcion(String message) {
        super(message);
    }

    public InfraestructuraExcepcion(String message, Throwable cause) {
        super(message, cause);
    }
}