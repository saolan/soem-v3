package ec.com.tecnointel.soem.egreso.registroInt;

import ec.com.tecnointel.soem.egreso.modelo.EgreNota;
import jakarta.ejb.Local;

@Local
public interface EgreNotaRegisInt {

	public Object insertar(EgreNota egreNota) throws Exception;

	public void modificar(EgreNota egreNota) throws Exception;

	public void eliminar(EgreNota egreNota) throws Exception;

	public EgreNota buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
