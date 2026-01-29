	package ec.com.saolan.soem.asistencia.registroInt;

import ec.com.saolan.soem.asistencia.modelo.FeriMapa;
import jakarta.ejb.Local;

@Local
public interface FeriMapaRegisInt {

	public Object insertar(FeriMapa feriMapa) throws Exception;

	public void modificar(FeriMapa feriMapa) throws Exception;

	public void eliminar(FeriMapa feriMapa) throws Exception;

	public FeriMapa buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
