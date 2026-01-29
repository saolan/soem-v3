package ec.com.saolan.soem.asistencia.registroInt;

import ec.com.saolan.soem.asistencia.modelo.Ausencia;
import jakarta.ejb.Local;

@Local
public interface AusenciaRegisInt {

	public Object insertar(Ausencia ausencia) throws Exception;

	public void modificar(Ausencia ausencia) throws Exception;

	public void eliminar(Ausencia ausencia) throws Exception;

	public Ausencia buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
