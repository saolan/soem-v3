package ec.com.saolan.soem.asistencia.registroInt;

import ec.com.saolan.soem.asistencia.modelo.HoraPlan;
import jakarta.ejb.Local;

@Local
public interface HoraPlanRegisInt {

	public Object insertar(HoraPlan horaPlan) throws Exception;

	public void modificar(HoraPlan horaPlan) throws Exception;

	public void eliminar(HoraPlan horaPlan) throws Exception;

	public HoraPlan buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
