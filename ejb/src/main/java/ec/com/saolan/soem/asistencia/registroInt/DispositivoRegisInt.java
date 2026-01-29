package ec.com.saolan.soem.asistencia.registroInt;

import ec.com.saolan.soem.asistencia.modelo.Dispositivo;
import jakarta.ejb.Local;

@Local
public interface DispositivoRegisInt {

	public Object insertar(Dispositivo dispositivo) throws Exception;

	public void modificar(Dispositivo dispositivo) throws Exception;

	public void eliminar(Dispositivo dispositivo) throws Exception;

	public Dispositivo buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
