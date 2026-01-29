package ec.com.tecnointel.soem.contabilidad.registroInt;

import java.util.List;

import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviEgre;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviIngr;
import ec.com.tecnointel.soem.tesoreria.modelo.FpmeFormPago;
import jakarta.ejb.Local;

@Local
public interface TransaccionFpmeInt {
	
	Integer contabilizarCobro(FormPagoMoviEgre fpme) throws Exception;

	Integer contabilizarAnticipo(FormPagoMoviEgre fpme) throws Exception;

	FormPagoMoviIngr crearDeposito(FpmeFormPago fpmeFp) throws Exception;

	void insertarFpmiDepositos(List<FormPagoMoviIngr> fpmis) throws Exception;
		
}
