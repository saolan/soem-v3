package ec.com.saolan.soem.asistencia.registroInt;

import ec.com.saolan.soem.asistencia.modelo.EmplCargVaca;
import jakarta.ejb.Local;

@Local
public interface EmplCargVacaRegisInt {

	public Object insertar(EmplCargVaca emplCargVaca) throws Exception;

	public void modificar(EmplCargVaca emplCargVaca) throws Exception;

	public void eliminar(EmplCargVaca emplCargVaca) throws Exception;

	public EmplCargVaca buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
