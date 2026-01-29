package ec.com.saolan.soem.asistencia.registroInt;

import ec.com.saolan.soem.asistencia.modelo.FeriRecu;
import jakarta.ejb.Local;

@Local
public interface FeriRecuRegisInt {

	public Object insertar(FeriRecu feriRecu) throws Exception;

	public void modificar(FeriRecu feriRecu) throws Exception;

	public void eliminar(FeriRecu feriRecu) throws Exception;

	public FeriRecu buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
