package ec.com.saolan.soem.asistencia.registroInt;

import ec.com.saolan.soem.asistencia.modelo.HoraExtr;
import jakarta.ejb.Local;

@Local
public interface HoraExtrRegisInt {

	public Object insertar(HoraExtr horaExtr) throws Exception;

	public void modificar(HoraExtr horaExtr) throws Exception;

	public void eliminar(HoraExtr horaExtr) throws Exception;

	public HoraExtr buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
