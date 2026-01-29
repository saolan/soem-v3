package ec.com.tecnointel.soem.parametro.registroInt;

import ec.com.tecnointel.soem.parametro.modelo.FormPago;
import jakarta.ejb.Local;

@Local
public interface FormPagoRegisInt {

	public Object insertar(FormPago formPago) throws Exception;

	public void modificar(FormPago formPago) throws Exception;

	public void eliminar(FormPago formPago) throws Exception;

	public FormPago buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
