package ec.com.saolan.soem.asistencia.registroInt;

import ec.com.saolan.soem.asistencia.modelo.Horario;
import jakarta.ejb.Local;

@Local
public interface HorarioRegisInt {

	public Object insertar(Horario horario) throws Exception;

	public void modificar(Horario horario) throws Exception;

	public void eliminar(Horario horario) throws Exception;

	public Horario buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
