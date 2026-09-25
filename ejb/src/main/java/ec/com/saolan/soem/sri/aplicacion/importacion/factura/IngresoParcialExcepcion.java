package ec.com.saolan.soem.sri.aplicacion.importacion.factura;

import ec.com.saolan.soem.compartido.excepcion.ValidacionNegocioExcepcion;
import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;

/**
 * Se lanza cuando no se pudo cargar el detalle (IngrDetas) de una factura
 * importada, pero la cabecera, el proveedor y los impuestos/retenciones ya se
 * cargaron correctamente. Transporta el Ingreso parcial para que el
 * llamador pueda mostrarlo igualmente al usuario.
 */
public class IngresoParcialExcepcion extends ValidacionNegocioExcepcion {

	private static final long serialVersionUID = 1L;

	private final Ingreso ingresoParcial;

	public IngresoParcialExcepcion(String message, Ingreso ingresoParcial, Throwable cause) {
		super(message, cause);
		this.ingresoParcial = ingresoParcial;
	}

	public Ingreso getIngresoParcial() {
		return ingresoParcial;
	}
}
