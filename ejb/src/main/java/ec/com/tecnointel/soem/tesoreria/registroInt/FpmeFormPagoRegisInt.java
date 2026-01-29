package ec.com.tecnointel.soem.tesoreria.registroInt;

import ec.com.tecnointel.soem.tesoreria.modelo.FpmeFormPago;
import jakarta.ejb.Local;

@Local
public interface FpmeFormPagoRegisInt {

	public Object insertar(FpmeFormPago fpmeFormPago) throws Exception;

	public void modificar(FpmeFormPago fpmeFormPago) throws Exception;

	public void eliminar(FpmeFormPago fpmeFormPago) throws Exception;

	public FpmeFormPago buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
