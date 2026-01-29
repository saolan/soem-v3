package ec.com.saolan.soem.asistencia.registroInt;

import ec.com.saolan.soem.asistencia.modelo.HoraDeta;
import jakarta.ejb.Local;

@Local
public interface HoraDetaRegisInt {

	public Object insertar(HoraDeta horaDeta) throws Exception;

	public void modificar(HoraDeta horaDeta) throws Exception;

	public void eliminar(HoraDeta horaDeta) throws Exception;

	public HoraDeta buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
