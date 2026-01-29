package ec.com.tecnointel.soem.egreso.registroInt;

import ec.com.tecnointel.soem.egreso.modelo.PersClie;
import jakarta.ejb.Local;

@Local
public interface PersClieRegisInt {

	public Object insertar(PersClie persClie) throws Exception;

	public void modificar(PersClie persClie) throws Exception;

	public void eliminar(PersClie persClie) throws Exception;

	public PersClie buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
