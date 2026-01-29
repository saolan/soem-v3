package ec.com.saolan.soem.asistencia.registroInt;

import ec.com.saolan.soem.asistencia.modelo.EmplDisc;
import jakarta.ejb.Local;

@Local
public interface EmplDiscRegisInt {

	public Object insertar(EmplDisc emplDisc) throws Exception;

	public void modificar(EmplDisc emplDisc) throws Exception;

	public void eliminar(EmplDisc emplDisc) throws Exception;

	public EmplDisc buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
