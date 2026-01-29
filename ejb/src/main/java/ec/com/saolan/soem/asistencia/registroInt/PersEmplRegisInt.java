package ec.com.saolan.soem.asistencia.registroInt;

import ec.com.saolan.soem.asistencia.modelo.PersEmpl;
import jakarta.ejb.Local;

@Local
public interface PersEmplRegisInt {

	public Object insertar(PersEmpl persEmpl) throws Exception;

	public void modificar(PersEmpl persEmpl) throws Exception;

	public void eliminar(PersEmpl persEmpl) throws Exception;

	public PersEmpl buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
