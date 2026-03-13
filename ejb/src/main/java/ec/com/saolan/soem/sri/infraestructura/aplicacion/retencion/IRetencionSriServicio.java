package ec.com.saolan.soem.sri.infraestructura.aplicacion.retencion;

import ec.com.tecnointel.soem.ingreso.modelo.Retencion;
import jakarta.ejb.Local;

@Local
public interface IRetencionSriServicio {

	Retencion procesarRetencionSri(String claveAcceso);

}
