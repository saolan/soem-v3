package ec.com.tecnointel.soem.seguridad.registroInt;

import ec.com.tecnointel.soem.seguridad.modelo.RolFormPago;
import jakarta.ejb.Local;

@Local
public interface RolFormPagoRegisInt {

	public Object insertar(RolFormPago rolFormPago) throws Exception;

	public void modificar(RolFormPago rolFormPago) throws Exception;

	public void eliminar(RolFormPago rolFormPago) throws Exception;

	public RolFormPago buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
