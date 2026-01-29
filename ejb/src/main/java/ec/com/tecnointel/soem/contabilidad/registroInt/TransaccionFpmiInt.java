package ec.com.tecnointel.soem.contabilidad.registroInt;

import java.util.List;

import ec.com.tecnointel.soem.parametro.modelo.TranPlanDeta;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviIngr;
import jakarta.ejb.Local;

@Local
public interface TransaccionFpmiInt {
	
	Integer contabilizarPago(FormPagoMoviIngr fpmi) throws Exception;

//	Pago de gastos
	Integer contabilizarPago(FormPagoMoviIngr fpmi, List<TranPlanDeta> tranPlanDetas) throws Exception;
	
	Integer contabilizarAnticipo(FormPagoMoviIngr fpmi) throws Exception;

	Integer contabilizarDeposito(FormPagoMoviIngr fpmi) throws Exception;

	
	
}
