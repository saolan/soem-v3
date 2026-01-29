package ec.com.saolan.soem.asistencia.registroInt;

import ec.com.saolan.soem.asistencia.modelo.EmplHoraPlan;
import jakarta.ejb.Local;

@Local
public interface EmplHoraPlanRegisInt {

	public Object insertar(EmplHoraPlan emplHoraPlan) throws Exception;

	public void modificar(EmplHoraPlan emplHoraPlan) throws Exception;

	public void eliminar(EmplHoraPlan emplHoraPlan) throws Exception;

	public EmplHoraPlan buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
