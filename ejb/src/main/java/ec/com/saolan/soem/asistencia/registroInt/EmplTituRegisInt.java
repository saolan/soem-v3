package ec.com.saolan.soem.asistencia.registroInt;

import ec.com.saolan.soem.asistencia.modelo.EmplTitu;
import jakarta.ejb.Local;

@Local
public interface EmplTituRegisInt {

	public Object insertar(EmplTitu emplTitu) throws Exception;

	public void modificar(EmplTitu emplTitu) throws Exception;

	public void eliminar(EmplTitu emplTitu) throws Exception;

	public EmplTitu buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
