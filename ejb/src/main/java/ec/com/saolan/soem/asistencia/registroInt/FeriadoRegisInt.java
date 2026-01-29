package ec.com.saolan.soem.asistencia.registroInt;

import ec.com.saolan.soem.asistencia.modelo.Feriado;
import jakarta.ejb.Local;

@Local
public interface FeriadoRegisInt {

	public Object insertar(Feriado feriado) throws Exception;

	public void modificar(Feriado feriado) throws Exception;

	public void eliminar(Feriado feriado) throws Exception;

	public Feriado buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
