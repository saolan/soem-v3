package ec.com.tecnointel.soem.contabilidad.registroInt;

import ec.com.tecnointel.soem.contabilidad.modelo.Transaccion;
import jakarta.ejb.Local;

@Local
public interface TransaccionRegisInt {

	public Object insertar(Transaccion transaccion) throws Exception;

	public void modificar(Transaccion transaccion) throws Exception;

	public void eliminar(Transaccion transaccion) throws Exception;

	public Transaccion buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
