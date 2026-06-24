package ec.com.saolan.soem.compartido.excepcion;

import jakarta.ejb.ApplicationException;

/**
 * Excepción para validaciones funcionales del negocio.
 * Ejemplos:
 * Codigo no existe
 * Codigo duplicado
 * Cedula o ruc ya existe
 * productos no registrados
 * factura duplicada
 * stock insuficiente
 * proveedor inactivo
 * Descuento excede limite
 */
@ApplicationException(rollback = false, inherited = true)
public class ValidacionNegocioExcepcion extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ValidacionNegocioExcepcion(String message) {
        super(message);
    }

    public ValidacionNegocioExcepcion(String message, Throwable cause) {
        super(message, cause);
    }
}
