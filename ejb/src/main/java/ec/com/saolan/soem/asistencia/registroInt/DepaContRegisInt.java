package ec.com.saolan.soem.asistencia.registroInt;

import ec.com.saolan.soem.asistencia.modelo.DepaCont;
import jakarta.ejb.Local;

@Local
public interface DepaContRegisInt {

	public Object insertar(DepaCont depaCont) throws Exception;

	public void modificar(DepaCont depaCont) throws Exception;

	public void eliminar(DepaCont depaCont) throws Exception;

	public DepaCont buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
