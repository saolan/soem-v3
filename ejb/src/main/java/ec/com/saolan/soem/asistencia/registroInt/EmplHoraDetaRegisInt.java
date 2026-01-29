package ec.com.saolan.soem.asistencia.registroInt;

import ec.com.saolan.soem.asistencia.modelo.EmplHoraDeta;
import jakarta.ejb.Local;

@Local
public interface EmplHoraDetaRegisInt {

	public Object insertar(EmplHoraDeta emplHoraDeta) throws Exception;

	public void modificar(EmplHoraDeta emplHoraDeta) throws Exception;

	public void eliminar(EmplHoraDeta emplHoraDeta) throws Exception;

	public EmplHoraDeta buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
