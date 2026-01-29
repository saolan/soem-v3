package ec.com.tecnointel.soem.contabilidad.registroInt;

import ec.com.tecnointel.soem.egreso.modelo.Egreso;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviEgre;
import jakarta.ejb.Local;

@Local
public interface TransaccionVentaInt {

	Integer contabilizarVenta(Egreso egreso) throws Exception;
	
	Integer contabilizarCostoVenta(Egreso egreso) throws Exception;
	
	Integer contabilizarNotaCredito(Egreso egreso, FormPagoMoviEgre fpme) throws Exception;
}
