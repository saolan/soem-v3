package ec.com.tecnointel.soem.caja.registroInt;

import ec.com.tecnointel.soem.caja.modelo.Caja;
import jakarta.ejb.Local;

@Local
public interface CajaRegisInt {

	public Object insertar(Caja caja) throws Exception;

	public void modificar(Caja caja) throws Exception;

	public void eliminar(Caja caja) throws Exception;

	public Caja buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
