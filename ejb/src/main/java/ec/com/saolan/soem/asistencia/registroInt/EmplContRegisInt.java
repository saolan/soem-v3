package ec.com.saolan.soem.asistencia.registroInt;

import ec.com.saolan.soem.asistencia.modelo.EmplCont;
import jakarta.ejb.Local;

@Local
public interface EmplContRegisInt {

	public Object insertar(EmplCont emplCont) throws Exception;

	public void modificar(EmplCont emplCont) throws Exception;

	public void eliminar(EmplCont emplCont) throws Exception;

	public EmplCont buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
