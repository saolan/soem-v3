package ec.com.saolan.soem.asistencia.registroInt;

import ec.com.saolan.soem.asistencia.modelo.KardVaca;
import jakarta.ejb.Local;

@Local
public interface KardVacaRegisInt {

	public Object insertar(KardVaca kardVaca) throws Exception;

	public void modificar(KardVaca kardVaca) throws Exception;

	public void eliminar(KardVaca kardVaca) throws Exception;

	public KardVaca buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
