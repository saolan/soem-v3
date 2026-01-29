package ec.com.saolan.soem.asistencia.registroInt;

import ec.com.saolan.soem.asistencia.modelo.DepaNive;
import jakarta.ejb.Local;

@Local
public interface DepaNiveRegisInt {

	public Object insertar(DepaNive depaNive) throws Exception;

	public void modificar(DepaNive depaNive) throws Exception;

	public void eliminar(DepaNive depaNive) throws Exception;

	public DepaNive buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
