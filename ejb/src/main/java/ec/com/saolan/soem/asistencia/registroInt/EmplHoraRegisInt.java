package ec.com.saolan.soem.asistencia.registroInt;

import ec.com.saolan.soem.asistencia.modelo.EmplHora;
import jakarta.ejb.Local;

@Local
public interface EmplHoraRegisInt {

	public Object insertar(EmplHora emplHora) throws Exception;

	public void modificar(EmplHora emplHora) throws Exception;

	public void eliminar(EmplHora emplHora) throws Exception;

	public EmplHora buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
