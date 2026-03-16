package ec.com.saolan.soem.sri.infraestructura.aplicacion.retencion;

import java.util.List;

import ec.com.tecnointel.soem.ingreso.modelo.ReteDeta;
import jakarta.ejb.Local;

@Local
public interface IRetencionServicio {
	
	void calcularReteDeta (List<ReteDeta> reteDetas);

}
